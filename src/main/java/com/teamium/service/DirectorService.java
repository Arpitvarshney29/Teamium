package com.teamium.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.Director;
import com.teamium.domain.prod.resources.staff.StaffEmail;
import com.teamium.domain.prod.resources.staff.StaffTelephone;
import com.teamium.domain.prod.resources.staff.UserSetting;
import com.teamium.dto.DirectorDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.DirectorRepository;
import com.teamium.repository.PersonRepository;
import com.teamium.utils.CommonUtil;

@Service
public class DirectorService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private PersonRepository personRepository;

	private AuthenticationService authenticationService;

	@Autowired
	public DirectorService(DirectorRepository directorRepository, PersonRepository personRepository,
			AuthenticationService authenticationService) {
		this.personRepository = personRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or upadate director
	 * 
	 * @param directorDTO
	 * 
	 * @return DirectorDTO
	 */
	public DirectorDTO saveOrUpdateDirector(DirectorDTO directorDTO) {
		logger.info("Inside DirectorService :: saveOrUpdateDirector(), To save or upadate director: " + directorDTO);

		Director director = new Director();
		UserSetting userSetting = new UserSetting();

		if (directorDTO.getId() != null) {
			director = findDirectorById(directorDTO.getId());
			validateDirectorForUpdateEmailAndTelephone(directorDTO);
		} else {
			validateDirectorForSaveEmailAndTelephone(directorDTO);
		}
		validateDirectorForName(directorDTO);

		director.setFirstName(directorDTO.getFirstName().trim());
		if (directorDTO.getLastName() != null) {
			director.setName(directorDTO.getLastName().trim());
		}

		Set<StaffEmail> staffEmail = new HashSet<StaffEmail>();
		StaffEmail staff = new StaffEmail(directorDTO.getDirectorEmail(), true);
		staffEmail.add(staff);

		Set<StaffTelephone> telephones = new HashSet<StaffTelephone>();
		StaffTelephone staffTelephone = new StaffTelephone(directorDTO.getDirectorTelephone(), true);
		telephones.add(staffTelephone);

		userSetting.setEmails(staffEmail);
		userSetting.setTelephones(telephones);

		director.setUserSetting(userSetting);
		Person person = (Person) this.personRepository.save(director);
		logger.info("Returning from DirectorService::saveOrUpdateDirector().");
		return new DirectorDTO(person);

	}

	/**
	 * To find director by id
	 * 
	 * @param directorDtoId
	 * 
	 * @return Director
	 */
	private Director findDirectorById(Long directorDtoId) {
		logger.info("Inside  DirectorService::findDirectorById(), To find director by id: " + directorDtoId);
		Director director = (Director) personRepository.getPersonByDiscriminatorAndId(Director.class, directorDtoId);
		if (director == null) {
			logger.error("Invalid director id.");
			throw new NotFoundException("Invalid director id.");
		}
		logger.info("Returning from DirectorService::findDirectorById().");
		return director;
	}

	/**
	 * To validate director for name.
	 *
	 * @param directorDTO
	 */
	private void validateDirectorForName(DirectorDTO directorDTO) {
		logger.info("Inside DirectorService::validateDirector(), To validate save/update director: " + directorDTO);
		Director director = new Director();
		// check valid first name.
		if (StringUtils.isBlank(directorDTO.getFirstName())
				|| !directorDTO.getFirstName().matches("^[a-zA-ZÀ-ÖØ-öø-ÿ]{1,30}+$")) {
			logger.info("Please enter valid first name.");
			throw new UnprocessableEntityException("Please enter valid first name.");
		}
		logger.info("Returning from DirectorService :: validationDirector()");
	}

	/**
	 * To validate save director for save email and telephone
	 * 
	 * @param directorDTO
	 */
	private void validateDirectorForSaveEmailAndTelephone(DirectorDTO directorDTO) {
		// checking already existing email with another existing-user
		if (directorDTO.getDirectorEmail() != null) {
			List<Person> personEmail = personRepository.findByEmail(directorDTO.getDirectorEmail());
			if (personEmail.size() != 0) {
				logger.info("Email already exists.");
				throw new UnprocessableEntityException("Email already exists.");
			}
			validateRegexForEmail(directorDTO);
		}
		// // checking already existing Telephone with another existing-user
		if (directorDTO.getDirectorTelephone() != null) {
			List<Person> personTelephone = personRepository.findByTelephone(directorDTO.getDirectorTelephone());
			if (personTelephone.size() != 0) {
				logger.info("Telephone already exists.");
				throw new UnprocessableEntityException("Telephone already exists.");
			}
			validateRegexForTelephone(directorDTO);
		}
		logger.info("Returning from DirectorService :: validateDirectorForSaveEmailAndTelephone()");

	}

	/**
	 * To validate director for update email and telephone.
	 * 
	 * @param directorDTO
	 */
	private void validateDirectorForUpdateEmailAndTelephone(DirectorDTO directorDTO) {
		// checking already existing email with same existing-user
		if (directorDTO.getDirectorEmail() != null) {
		List<Person> personEmail = personRepository.findByEmail(directorDTO.getDirectorEmail());
		if (personEmail.size() != 0 && (directorDTO.getId() != null
				&& (personEmail.get(0).getId().longValue() != directorDTO.getId().longValue()))) {
			logger.info("Email already exists.");
			throw new UnprocessableEntityException("Email already exists.");
		}
		validateRegexForEmail(directorDTO);
		}

		// checking already existing Telephone with same existing-user
		if (directorDTO.getDirectorTelephone() != null) {
		List<Person> personTelephone = personRepository.findByTelephone(directorDTO.getDirectorTelephone());
		if (personTelephone.size() != 0 && (directorDTO.getId() != null
				&& (personTelephone.get(0).getId().longValue() != directorDTO.getId().longValue()))) {
			logger.info("Telephone already exists.");
			throw new UnprocessableEntityException("Telephone already exists.");
		}
		validateRegexForTelephone(directorDTO);
		}
		logger.info("Returning from DirectorService :: validateDirectorForUpdateEmailAndTelephone()");
	}

	/**
	 * To validate regex for email.
	 * 
	 * @param directorDTO
	 */
	private void validateRegexForEmail(DirectorDTO directorDTO) {
		// check email is valid as per regex and length.
		if (directorDTO.getDirectorEmail() != null && (!CommonUtil.isEmailValid(directorDTO.getDirectorEmail())
				|| directorDTO.getDirectorEmail().length() > 65)) {
			logger.error("Please enter a valid email.");
			throw new UnprocessableEntityException("Please enter a valid email.");
		}
		logger.info("Returning from DirectorService :: ValidateRegexForEmail()");
	}

	/**
	 * To validate for regex for telephopne.
	 * 
	 * @param directorDTO
	 */
	private void validateRegexForTelephone(DirectorDTO directorDTO) {
		// check telephone is valid as per regex and length.
		String tele = directorDTO.getDirectorTelephone();
		if (tele != null) {

			if (tele.split("-").length == 2) {
				if (CommonUtil.isTelephoneCodeNotValid(tele.split("-")[0])) {
					logger.error("Please enter a valid telephone.");
					throw new UnprocessableEntityException("Please enter a valid telephone.");
				}
				if (!CommonUtil.isTelephoneNumberValid(tele)) {
					logger.error("Please enter a valid telephone.");
					throw new UnprocessableEntityException("Please enter a valid telephone.");
				}
				if (tele.split("-")[0].length() > 8 || tele.split("-")[1].length() < 6
						|| tele.split("-")[1].length() > 10 || tele.length() > 17 || tele.length() < 10) {
					logger.error("Please enter a valid telephone.");
					throw new UnprocessableEntityException("Please enter a valid telephone.");
				}
			} else if (tele.split("-").length >= 2) {
				logger.error("Please enter a valid telephone.");
				throw new UnprocessableEntityException("Please enter a valid telephone.");
			} else {
				if (!CommonUtil.isMobileNumberValid(directorDTO.getDirectorTelephone())) {
					logger.error("Please enter a valid telephone.");
					throw new UnprocessableEntityException("Please enter a valid telephone.");
				}
			}
		}
		logger.info("Returning from DirectorService :: ValidateRegexForTelephone()");
	}

	/**
	 * TO get directors
	 * 
	 * @return List<DirectorDTO>.
	 */
	public List<DirectorDTO> getDirectors() {
		logger.info("Inside DirectorService::getDirector(), To get directors: ");
		List<DirectorDTO> directors = (List<DirectorDTO>) personRepository.getPersonsByDiscriminator(Director.class)
				.stream().map(dir -> new DirectorDTO((Person) dir)).collect(Collectors.toList());
		logger.info("Returning from DirectorService::getDirectors().");
		return directors;
	}

	/**
	 * To get director by id.
	 * 
	 * @param directorId
	 * 
	 * @return DirectorDTO
	 */
	public DirectorDTO getDirector(Long directorId) {
		logger.info("Inside DirectorService::getDirector(), To  get director by id: " + directorId);
		Person person = personRepository.getPersonByDiscriminatorAndId(Director.class, directorId);
		if (person == null) {
			logger.error("Director id is not valid.");
			throw new NotFoundException("Invalid director id.");
		}
		logger.info("Returning from DirectorService::getDirector()");
		return new DirectorDTO(person);
	}

	/**
	 * To remove director by id
	 * 
	 * @param directorId
	 */
	public void removeDirectorById(Long directorId) {
		logger.info("Inside DirectorService::removeDirectorById(), To remove director by id : " + directorId);
		if (!authenticationService.isAdmin()) {
			logger.error("You do not have authority to remove director.");
			throw new UnauthorizedException("You do not have authority to remove director.");
		}
		Person person = personRepository.getPersonByDiscriminatorAndId(Director.class, directorId);
		if (person == null) {
			logger.error("Director id is not valid.");
			throw new NotFoundException("Invalid director id.");
		}
		personRepository.delete(directorId);
		logger.info("Returning from DirectorService :: removeDirectorById()");
	}

}
