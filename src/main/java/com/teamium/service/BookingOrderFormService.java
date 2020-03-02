package com.teamium.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.projects.order.BookingOrderForm;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder.OrderType;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingOrderFormRepository;

@Service
public class BookingOrderFormService {

	private BookingOrderFormRepository bookingOrderFormRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public BookingOrderFormService(BookingOrderFormRepository bookingOrderFormRepository) {
		this.bookingOrderFormRepository = bookingOrderFormRepository;
	}

	/**
	 * To save Update BookingOrderForm
	 * 
	 * @param bookingOrderForm
	 * @return
	 */
	public BookingOrderForm saveOrUpdateBookingOrderForm(BookingOrderForm bookingOrderForm) {
		logger.info("Inside BookingOrderFormService :: saveOrUpdateBookingOrderForm(" + bookingOrderForm + ").");

		if (bookingOrderForm.getId() != null) {

		}
		validateBookingOrderForm(bookingOrderForm);
		BookingOrderForm bookingOrderFormByType = bookingOrderFormRepository
				.findByFormType(bookingOrderForm.getFormType());
		if (bookingOrderForm.getId() != null) {
			BookingOrderForm bookingOrderFormById = findById(bookingOrderForm.getId());
			if (bookingOrderFormByType != null && (bookingOrderFormById.getId() != bookingOrderFormByType.getId())) {
				logger.error("Order form  already exist with given name.");
				throw new UnprocessableEntityException("Order form  already exist with given name.");
			}
		} else if (bookingOrderFormByType != null) {
			logger.error("Order form  already exist with given name.");
			throw new UnprocessableEntityException("Order form  already exist with given name.");
		}

		BookingOrderForm bookingOrderFormDB = new BookingOrderForm(bookingOrderForm.getId(),
				bookingOrderForm.getVersion(), bookingOrderForm.getFormType(), bookingOrderForm.getOrderType(),
				bookingOrderForm.getKeyword());
		logger.info("Returning from BookingOrderFormService :: saveOrUpdateBookingOrderForm");
		return bookingOrderFormRepository.save(bookingOrderFormDB);

	}

	/**
	 * To get BookingOrderForm by id.
	 * 
	 * @param id
	 * @return BookingOrderForm
	 */
	public BookingOrderForm findById(long id) {
		logger.info("Inside BookingOrderFormService :: findById(" + id + ").");
		BookingOrderForm bookingOrderForm = bookingOrderFormRepository.findOne(id);
		if (bookingOrderForm == null) {
			logger.info("Invalid BookingOrderForm id.");
			throw new NotFoundException("Invalid BookingOrderForm id.");
		}
		logger.info("Returning from BookingOrderFormService :: findById(" + id + ").");
		return bookingOrderForm;
	}

	/**
	 * To get list of BookingOrderForm
	 * 
	 * @return List<BookingOrderForm>
	 */
	public List<BookingOrderForm> findAll() {
		logger.info("Inside BookingOrderFormService :: findAll().");
		return bookingOrderFormRepository.findAll();
	}

	/**
	 * To delete BookingOrderForm by id.
	 * 
	 * @param id
	 */
	public void deleteBookingOrderFormById(long id) {
		logger.info("Inside BookingOrderFormService :: deleteBookingOrderFormById(" + id + ").");
		findById(id);
		bookingOrderFormRepository.delete(id);
		logger.info("Returning after deleting booking order form.");

	}

	/**
	 * To validate BookingOrderForm
	 * 
	 * @param bookingOrderForm
	 */
	private void validateBookingOrderForm(BookingOrderForm bookingOrderForm) {
		logger.info("Inside BookingOrderFormService :: validateBookingOrderForm");
		if (StringUtils.isBlank(bookingOrderForm.getFormType())) {
			logger.error("Form type can not be null.");
			throw new UnprocessableEntityException("Form type can not be null.");
		}
		if (StringUtils.isBlank(bookingOrderForm.getOrderType())) {
			logger.error("Order type can not be null.");
			throw new UnprocessableEntityException("Order type can not be null.");
		}
		if (!OrderType.getAllOrderTypes().contains(bookingOrderForm.getOrderType().toString())) {
			logger.error("Invalid order type.");
			throw new UnprocessableEntityException("Invalid order type.");
		}
		if (bookingOrderForm.getKeyword() == null) {
			logger.error("Keywords can not be null.");
			throw new UnprocessableEntityException("Keywords can not be null.");
		}
		if (bookingOrderForm.getKeyword().isEmpty()) {
			logger.error("Atleast one Keyword required.");
			throw new UnprocessableEntityException("Atleast one Keyword required.");
		}
		if (bookingOrderForm.getKeyword().stream().anyMatch(k -> StringUtils.isBlank(k.getKey()))) {
			logger.error("Keyword name is required.");
			throw new UnprocessableEntityException("Keyword name is required.");
		}
		List<String> keys = bookingOrderForm.getKeyword().stream().map(keyword -> keyword.getKey())
				.collect(Collectors.toList());
		if (IntStream.range(0, keys.size())
				.filter(st1 -> IntStream.range(st1 + 1, keys.size())
						.filter(st2 -> keys.get(st1).equalsIgnoreCase(keys.get(st2))).findFirst().orElse(-1) > -1)
				.findFirst().orElse(-1) > -1) {
			logger.error("Duplicate keys should not be present in keywords.");
			throw new UnprocessableEntityException("Duplicate keys should not be present in keywords.");
		}
		logger.info("Returning after validating  booking order form.");
	}

	/**
	 * To get bookingOrderForm by type;
	 * 
	 * @param formType
	 * @return
	 */
	public BookingOrderForm findByType(String formType) {
		logger.info("Inside BookingOrderFormService :: findByType(" + formType + ")");
		BookingOrderForm bookingOrderForm = this.bookingOrderFormRepository.findByFormType(formType);
		if (bookingOrderForm == null) {
			logger.error("No Order Form present with given type.");
			throw new NotFoundException("No Order Form present with given type.");
		}
		return bookingOrderForm;

	}
}
