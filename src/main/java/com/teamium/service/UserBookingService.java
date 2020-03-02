package com.teamium.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.UserBooking;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.RatedFunctionDTO;
import com.teamium.dto.ResourceDTO;
import com.teamium.dto.UserBookingDTO;
import com.teamium.dto.WeekDurationDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.UserBookinRepository;

@Service
public class UserBookingService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private UserBookinRepository userBookinRepository;
	private StaffMemberService staffMemberService;
	private BookingService bookingService;
	private RecordService recordService;

	@Autowired
	public UserBookingService(UserBookinRepository userBookinRepository, StaffMemberService staffMemberService,
			BookingService bookingService, RecordService recordService) {
		this.userBookinRepository = userBookinRepository;
		this.staffMemberService = staffMemberService;
		this.bookingService = bookingService;
		this.recordService = recordService;
	}

	/**
	 * save or update UserBooking.
	 * 
	 * @param userBookingDTO
	 * @return object of UserBookingDTO.
	 */
	public UserBookingDTO saveOrUpdateUserBooking(UserBookingDTO userBookingDTO) {
		logger.info("Inside saveOrUpdateUserBooking( " + userBookingDTO + " )");
		validateUserBooking(userBookingDTO);
		StaffMember staffMember = this.staffMemberService.validateStaffMember(userBookingDTO.getStaffMemberId());
		UserBooking userBooking = null;
		if (userBookingDTO.getId() != null) {
			userBooking = userBookinRepository.findOne(userBookingDTO.getId());
			if (userBooking == null) {
				logger.info("Invalid user booking id.");
				throw new NotFoundException("Invalid user booking id.");
			}
			userBooking = userBookingDTO.getUserBookingEntity(userBooking);
		} else {
			userBooking = userBookingDTO.getUserBookingEntity(new UserBooking());
		}
		userBooking.setStaffMember(staffMember);

		userBooking = userBookinRepository.save(userBooking);
		Booking booking = null;
		if (userBooking.getBookingId() != null) {
			booking = bookingService.findBooking(userBooking.getBookingId());
			if (booking != null) {
				userBooking.setStartTime(booking.getFrom().getTime());
				userBooking.setEndTime(booking.getTo().getTime());
			}
		}

		userBookingDTO = new UserBookingDTO(userBooking);

		if (booking != null) {
			userBookingDTO.setFunctionName(booking.getFunction().getQualifiedName());
			if (booking.getRecord() instanceof Project) {
				Project project = ((Project) booking.getRecord());
				if (project.getTheme() != null && project.getTheme().getKey() != null) {
					userBookingDTO.setTheme(project.getTheme().getKey());
				}
			}
		}

		logger.info("successfully return from saveOrUpdateUserBooking( " + userBookingDTO + " )");
		return userBookingDTO;
	}

	/**
	 * Fetch all UserBooking For Particular User.
	 * 
	 * @return list of UserBookingDTO.
	 */
	public List<UserBookingDTO> findAllUserBookingByStaffMemberId(Long staffMemberId) {
		logger.info("Inside findAllUserBooking");
		List<UserBookingDTO> userBookingDTOs = this.userBookinRepository
				.findAllUserBookingByStaffMemberId(staffMemberId).stream().map(dt -> {
					if (dt.getBookingId() != null) {
						Booking booking = bookingService.findBooking(dt.getBookingId());
						if (booking == null) {
							return null;
						} else {
							dt.setStartTime(booking.getFrom().getTime());
							dt.setEndTime(booking.getTo().getTime());
						}
					}
					return dt;
				}).filter(dt -> dt != null).map(dt -> {
					UserBookingDTO userBookingDTO = new UserBookingDTO(dt);
					if (userBookingDTO.getBookingId() != null) {
						Booking booking = bookingService.findBooking(userBookingDTO.getBookingId());
						userBookingDTO.setFunctionName(booking.getFunction().getQualifiedName());
						if (booking.getRecord() instanceof Project) {
							Project project = ((Project) booking.getRecord());
							if (project.getTheme() != null && project.getTheme().getKey() != null) {
								userBookingDTO.setTheme(project.getTheme().getKey());
							}
						}
					}
					return userBookingDTO;
				}).collect(Collectors.toList());
		logger.info("successfully return from findAllUserBooking :: " + userBookingDTOs.size());
		return userBookingDTOs;

	}

	/**
	 * Find UserBooking by id.
	 * 
	 * @param userBookingId
	 * @return object of UserBooking.
	 */
	public UserBooking findUserBookingById(Long userBookingId) {
		logger.info("Inside findUserBookingById( " + userBookingId + " )");
		if (userBookingId == null) {
			logger.info("Invalid userbooking id. ");
			throw new NotFoundException("Invalid userbooking id.");
		}
		UserBooking userBooking = userBookinRepository.findOne(userBookingId);
		if (userBooking == null) {
			logger.info("Invalid userbooking id. ");
			throw new NotFoundException("Invalid userbooking id.");
		}
		logger.info("Return successfully findUserBookingById( " + userBookingId + " )");
		return userBooking;
	}

	/**
	 * Find UserBooking by BookingEventid.
	 * 
	 * @param bookingEventId
	 * @return object of UserBooking.
	 */
	public UserBooking findUserBookingByBookingId(Long bookingId) {
		logger.info("Inside findUserBookingByBookingId( " + bookingId + " )");
		UserBooking userBooking = userBookinRepository.findUserBookingByBookingId(bookingId);
		logger.info("Return successfully findUserBookingByBookingId( " + bookingId + " )");
		return userBooking;
	}

	/**
	 * Validate User Booking.
	 * 
	 * @param userBookingDTO
	 */
	public void validateUserBooking(UserBookingDTO userBookingDTO) {
		logger.info("Inside validateUserBooking( " + userBookingDTO + " )");
		if (userBookingDTO.getStaffMemberId() == null) {
			logger.info("Invalid staff member id. ");
			throw new NotFoundException("Invalid staff member id.");
		}

		if (userBookingDTO.getUserStartTime() == null) {
			logger.info("start time can not empty.");
			throw new NotFoundException("start time can not empty.");
		}

		if (userBookingDTO.getUserEndTime() == null) {
			logger.info("end time can not empty.");
			throw new NotFoundException("end time can not empty.");
		}

		Date startDate = DateTime.parse(userBookingDTO.getUserStartTime()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toDate();
		Date endDate = DateTime.parse(userBookingDTO.getUserEndTime()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toDate();
		if (endDate.before(startDate)) {
			this.logger.info("start date can not be smaller than end date.");
			throw new NotFoundException("start date can not be smaller than end date.");
		}
		logger.info("successfully return from validateUserBooking( " + userBookingDTO + " )");
	}

	/**
	 * saveUserBooking
	 * 
	 * @param userBooking
	 */
	public UserBooking saveUserBooking(UserBooking userBooking) {
		return this.userBookinRepository.save(userBooking);
	}

	/**
	 * Delete User Booking By Id.
	 * 
	 * @param userBookingId
	 */
	public void deleteUserBooking(Long userBookingId) {
		logger.info("Inside deleteUserBooking( " + userBookingId + " )");
		UserBooking userBooking = findUserBookingById(userBookingId);
		if (userBooking.getBookingId() != null) {
			logger.info("Invalid userbooking id. ");
			throw new NotFoundException("Invalid userbooking id.");
		}
		userBookinRepository.delete(userBooking);
		logger.info("successfully return from deleteUserBooking( " + userBookingId + " )");
	}

	/**
	 * Fetch all Weekly Duration For Particular User.
	 * 
	 * @return list of WeekDurationDTO.
	 */
	public List<WeekDurationDTO> findWeeklyWorkingDurationForUser(Long staffMemberId) {
		logger.info("Inside findAllUserBooking");
		List<WeekDurationDTO> userBooking = this.userBookinRepository.findAllUserBookingByStaffMemberId(staffMemberId)
				.stream().filter(dt -> dt.getUserStartTime() != null && dt.getUserEndTime() != null)
				.map(dt -> new WeekDurationDTO(new DateTime(dt.getUserStartTime()), new DateTime(dt.getUserEndTime()),
						dt.getSick()))
				.collect(Collectors.toList());

		return calculateWeekAndTime(userBooking, new ArrayList<WeekDurationDTO>(), new DateTime(), 0, 3);
	}

	public List<WeekDurationDTO> findWeeklyWorkingDurationForUser(Long staffMemberId, Date startDate, Date endDate,
			String type) {
		logger.info("Inside findWeeklyWorkingDurationForUser");
		List<WeekDurationDTO> userBooking = this.userBookinRepository.findAllUserBookingByStaffMemberId(staffMemberId)
				.stream().filter(dt -> dt.getUserStartTime() != null && dt.getUserEndTime() != null)
				.map(dt -> new WeekDurationDTO(new DateTime(dt.getUserStartTime()), new DateTime(dt.getUserEndTime()),
						dt.getSick()))
				.collect(Collectors.toList());
		switch (type) {
		case Constants.TIME_REPORT_BY_WEEK:
			int weeks = Weeks.weeksBetween((new DateTime(startDate)).dayOfWeek().withMinimumValue().minusDays(1),
					new DateTime(endDate).dayOfWeek().withMaximumValue().plusDays(1)).getWeeks();
			return calculateWeekAndTime(userBooking, new ArrayList<WeekDurationDTO>(), new DateTime(startDate), 0,
					weeks);
		case Constants.ANNUAL_PERSONNEL_TIME_REPORT:
			int months = Months.monthsBetween(new DateTime(startDate).dayOfMonth().withMinimumValue(),
					new DateTime(endDate).dayOfMonth().withMaximumValue()).getMonths();
			return calculateMonthAndTime(userBooking, new ArrayList<WeekDurationDTO>(), new DateTime(startDate), 0,
					months);

		default:
			return null;

		}

	}

	/**
	 * Calculate current week and time for that particular week.
	 * 
	 * @param userBooking
	 * @param weekDurationDTOs
	 * @param st
	 * @param startWeek
	 * @param maxWeekNumber
	 * @return list of WeekDurationDTO.
	 */
	private List<WeekDurationDTO> calculateWeekAndTime(List<WeekDurationDTO> userBooking,
			List<WeekDurationDTO> weekDurationDTOs, DateTime st, int startWeek, int maxWeekNumber) {
		if (startWeek > maxWeekNumber - 1) {
			return weekDurationDTOs;
		}
		int weekNumber = st.plusWeeks(startWeek).weekOfWeekyear().get();
		DateTime startDate = st.plusWeeks(startWeek).dayOfWeek().withMinimumValue().minusDays(1).withHourOfDay(0)
				.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime endDate = st.plusWeeks(startWeek).dayOfWeek().withMaximumValue().minusDays(1).withHourOfDay(23)
				.withMinuteOfHour(59).withSecondOfMinute(0).withMillisOfSecond(0);
		long totalDuration = calculateTimeViaDate(new ArrayList<>(userBooking), startDate, endDate);
		weekDurationDTOs.add(new WeekDurationDTO(weekNumber, totalDuration));
		return calculateWeekAndTime(userBooking, weekDurationDTOs, st, ++startWeek, maxWeekNumber);
	}

	/**
	 * Calculate current week and time for that particular week.
	 * 
	 * @param userBooking
	 * @param weekDurationDTOs
	 * @param st
	 * @param startWeek
	 * @param maxWeekNumber
	 * @return list of WeekDurationDTO.
	 */
	private List<WeekDurationDTO> calculateMonthAndTime(List<WeekDurationDTO> userBooking,
			List<WeekDurationDTO> weekDurationDTOs, DateTime st, int startMonth, int maxMonthNumber) {
		if (startMonth > maxMonthNumber) {
			return weekDurationDTOs;
		}
		String monthName = st.plusMonths(startMonth).monthOfYear().getAsText() + " "
				+ st.plusMonths(startMonth).getYear() % 100;
		DateTime startDate = st.plusMonths(startMonth).dayOfMonth().withMinimumValue().withHourOfDay(0)
				.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime endDate = st.plusMonths(startMonth).dayOfMonth().withMaximumValue().withHourOfDay(23)
				.withMinuteOfHour(59).withSecondOfMinute(0).withMillisOfSecond(0);

		long totalDuration = calculateTimeViaDate(new ArrayList<>(userBooking), startDate, endDate);
		weekDurationDTOs.add(new WeekDurationDTO(monthName, totalDuration));
		return calculateMonthAndTime(userBooking, weekDurationDTOs, st, ++startMonth, maxMonthNumber);
	}

	/**
	 * Get total duration between two dates.
	 * 
	 * @param userBooking
	 * @param startDate
	 * @param endDate
	 * @return total duration.
	 */
	public long calculateTimeViaDate(List<Object> userBooking, DateTime startDate, DateTime endDate) {
		return calculateTime(new ArrayList<>(userBooking), dt -> {
			WeekDurationDTO weekDurationDTO = (WeekDurationDTO) dt;
			return weekDurationDTO.getUserStartTime() != null && weekDurationDTO.getUserEndTime() != null
					&& ((weekDurationDTO.getUserStartTime().isBefore(endDate)
							&& weekDurationDTO.getUserEndTime().isAfter(startDate)
							|| (weekDurationDTO.getUserStartTime().isEqual(startDate)
									&& weekDurationDTO.getUserEndTime().isEqual(endDate))));
		}, dt -> {
			WeekDurationDTO weekDurationDTO = (WeekDurationDTO) dt;
			if (weekDurationDTO.getUserStartTime().isBefore(startDate)) {
				return Minutes.minutesBetween(startDate, weekDurationDTO.getUserEndTime()).getMinutes();
			} else if (weekDurationDTO.getUserEndTime().isAfter(endDate)) {
				return Minutes.minutesBetween(weekDurationDTO.getUserStartTime(), endDate).getMinutes();
			}
			return Minutes.minutesBetween(weekDurationDTO.getUserStartTime(), weekDurationDTO.getUserEndTime())
					.getMinutes();
		});
	}

	/**
	 * To Calculate number of working hour for an user on weekly bases.
	 * 
	 * @param userBooking
	 * @param filterLogic
	 * @param calculationLogic
	 * @return time of particular week.
	 */
	private long calculateTime(List<Object> userBooking, Predicate<Object> filterLogic,
			Function<Object, Integer> calculationLogic) {
		AtomicLong totalMinutes = new AtomicLong();
		userBooking.stream().filter(filterLogic).collect(Collectors.toList()).forEach(dt -> {
			totalMinutes.addAndGet(calculationLogic.apply(dt));
		});
		return totalMinutes.get();
	}

	/**
	 * Method to save time-report add-on on booking
	 * 
	 * @param bookingDTO
	 */
	public UserBookingDTO saveTimeReportAddOn(BookingDTO bookingDTO) {
		logger.info("Inside UserBookingService :: saveTimeReportAddOn(), to save time-report add -on");
		Long projectId = bookingDTO.getProjectId();
		if (projectId == null || projectId == 0) {
			logger.info("Please provide valid project id");
			throw new UnprocessableEntityException("Please provide valid project id");
		}
		RatedFunctionDTO function = bookingDTO.getFunction();
		if (function == null || (function.getId() == null || function.getId() == 0)) {
			logger.info("Please provide valid function");
			throw new UnprocessableEntityException("Please provide valid function");
		}
		ResourceDTO<?> resource = bookingDTO.getResource();
		if (resource == null || (resource.getId() == null || resource.getId() == 0)) {
			logger.info("Please provide valid staff id");
			throw new UnprocessableEntityException("Please provide valid saff id");
		}
		String startTime = bookingDTO.getStart();
		if (StringUtils.isBlank(startTime)) {
			logger.info("Please provide valid start time");
			throw new UnprocessableEntityException("Please provide valid start time");
		}
		String endTime = bookingDTO.getEnd();
		if (StringUtils.isBlank(endTime)) {
			logger.info("Please provide valid end time");
			throw new UnprocessableEntityException("Please provide valid end time");
		}

		StaffMember staffMember = this.staffMemberService.validateStaffMember(resource.getId());
		if (staffMember == null) {
			logger.info("Staff not found with id : " + resource.getId());
			throw new NotFoundException("Staff not found");
		}
		StaffResource staffResource = staffMember.getResource();
		if (staffResource == null) {
			logger.info("Staff-Resource not found on staff with id : " + resource.getId());
			throw new NotFoundException("Staff-Resource not found");
		}
		Set<ResourceFunction> resourceFunctions = staffResource.getFunctions();
		if (resourceFunctions == null || resourceFunctions.isEmpty()) {
			logger.info("Given function not assigned on staff yet with function-id : " + function.getId());
			throw new UnprocessableEntityException("Given function not assigned on staff yet");
		}

		Optional<ResourceFunction> resFun = resourceFunctions.stream()
				.filter(entity -> entity.getFunction().getId() != null
						&& entity.getFunction().getId().longValue() == function.getId().longValue())
				.findFirst();
		if (resFun.isPresent()) {
			bookingDTO.getResource().setId(staffResource.getId());
		} else {
			logger.info("Given function not assigned on staff with function-id : " + function.getId());
			throw new UnprocessableEntityException("Given function not assigned on staff");
		}
		ProjectDTO recordDTO = (ProjectDTO) recordService.saveBudgetLine(projectId, bookingDTO, Project.class);

		// update start and end time of project
		Record record = recordService.validateRecordExistence(Project.class, projectId);
		record = recordService.updateStartAndEndTime(record);
		recordService.updateAndPersistRecord(record);
		logger.info("Successfully saved time-report on bookingId : " + recordDTO.getId());

		Booking booking = bookingService.findBooking(recordDTO.getNewlyBookingId());
		BookingEvent event = booking.getBookingEvent();

		UserBooking userBooking = findUserBookingByBookingId(booking.getId());
		if (userBooking == null) {
			userBooking = new UserBooking();
			if (event.getTheme() != null) {
				userBooking.setTheme(event.getTheme().getKey());
			}
		}
		userBooking.setName(event.getTitle());
		userBooking.setBookingId(booking.getId());
		userBooking.setStaffMember((StaffMember) event.getResource().getEntity());
		userBooking.setStartTime(event.getFrom().getTime());
		userBooking.setEndTime(event.getTo().getTime());
		userBooking.setUserStartTime(event.getFrom().getTime());
		userBooking.setUserEndTime(event.getTo().getTime());
		userBooking.setSick(bookingDTO.isSick());
		userBooking = saveUserBooking(userBooking);

		UserBookingDTO userBookingDTO = new UserBookingDTO(userBooking);
		if (booking != null) {
			userBookingDTO.setFunctionName(booking.getFunction().getQualifiedName());
			if (booking.getRecord() instanceof Project) {
				Project project = ((Project) booking.getRecord());
				if (project.getTheme() != null && project.getTheme().getKey() != null) {
					userBookingDTO.setTheme(project.getTheme().getKey());
				}
			}
		}
		return userBookingDTO;
	}

}