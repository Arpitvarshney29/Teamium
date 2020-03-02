package com.teamium.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teamium.constants.Constants;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.dto.EquipmentDTO;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.PackageRepository;

/**
 * A service class implementation for Equipment Controller
 *
 */
@Service
public class PackageService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private PackageRepository packageRepository;
	private EquipmentService equipmentService;
	private AuthenticationService authenticationService;

	@Autowired
	public PackageService(PackageRepository packageRepository, EquipmentService equipmentService,
			AuthenticationService authenticationService) {
		this.packageRepository = packageRepository;
		this.equipmentService = equipmentService;
		this.authenticationService = authenticationService;
	}

	/**
	 * Find all Equipment which are not assigned to any Package.
	 * 
	 * @return list of EquipmentResourceDTOs.
	 */
	public List<EquipmentDTO> findEquipmentForPackage(long equipmentId) {
		logger.info("Inside PackageService, findEquipmentForPackage");
		logger.info("equipmentId : " + equipmentId);
		Equipment equipment = equipmentService.findEquipment(equipmentId);

		EquipmentResource eq = getTopMostParent(equipment.getResource());

		List<EquipmentResource> equipmentResources = packageRepository.findEquipmentResourcesForPackage();
		logger.info("Returning from PackageService, findEquipmentForPackage : found " + equipmentResources.size());
		return equipmentResources.stream()
				.filter(eqResource -> eqResource == null || eq == null
						|| eqResource.getId().longValue() != eq.getId().longValue())
				.filter(eqResource -> eqResource == null || equipment.getResource() == null
						|| eqResource.getId().longValue() != equipment.getResource().getId().longValue())
				.map(eqResource -> new EquipmentDTO(eqResource.getEntity())).collect(Collectors.toList());

	}

	/**
	 * Find all Equipment on the bases of Selected Package.
	 * 
	 * @param packageId
	 * @return list of EquipmentResourceDTOs.
	 */
	public List<EquipmentDTO> findEquipmentForSelectedPackage(Long packageId) {
		logger.info("Inside PackageService, findEquipmentForSelectedPackage");
		logger.info("packageId : " + packageId);
		EquipmentResource equipmentResource = validateEquipmentResource(packageId);
		List<EquipmentResource> equipmentResources = packageRepository
				.findEquipmentResourcesForSelectedPackage(equipmentResource);
		logger.info(
				"Returning from PackageService, findEquipmentForSelectedPackage : found " + equipmentResources.size());
		return equipmentResources.stream().map(dt -> new EquipmentDTO(dt.getEntity())).collect(Collectors.toList());
	}

	/**
	 * Add Or Remove EquipmentResource to the Package.
	 * 
	 * @param packageId
	 * @param equipmentResourceId
	 * @return String
	 */
	public void addEquipmentResourceToPackage(Long packageId, List<Long> equipmentResourceId) {
		if (!authenticationService.isEquipmentManager()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save or update to equipment package.");

			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		logger.info("Inside PackageService, addEquipmentResourceToPackage");
		logger.info("packageId : " + packageId);
		logger.info("equipmentResourceId : " + equipmentResourceId);

		EquipmentResource equipmentResource = validateEquipmentResource(packageId);
		EquipmentResource parentResource = getTopMostParent(equipmentResource);

		List<EquipmentResource> equipmentResources = packageRepository
				.findEquipmentResourcesForSelectedPackage(equipmentResource);

		equipmentResources.stream().forEach(st -> st.setParent(null));

		List<EquipmentResource> newlyEquipmentResources = null;

		if ((equipmentResourceId != null && !equipmentResourceId.isEmpty())) {
			logger.info("Inside If condition");
			logger.info("Fetching list of EquipmentResource Id which are not assigned to any package.");

			List<Long> availableEquipmentResourcesIdForPackage = packageRepository.findEquipmentResourcesForPackage()
					.stream().map(equipmentRes -> equipmentRes.getId()).collect(Collectors.toList());

			logger.info("Fetched list of EquipmentResource Id which are not assigned to any package Found : "
					+ availableEquipmentResourcesIdForPackage);

			newlyEquipmentResources = equipmentResourceId.stream().map(resourceId -> {
				EquipmentResource euipResource = validateEquipmentResource(resourceId);
				if (equipmentResource.getId().longValue() == resourceId.longValue()) {
					logger.info("Child can not be a parent itself.");
					throw new UnprocessableEntityException("Child can not be a parent itself.");
				}
				if (parentResource != null && parentResource.getId().longValue() == resourceId.longValue()) {
					logger.info("Child can not be a parent.");
					throw new UnprocessableEntityException("Child can not be a parent.");
				}
				if (!availableEquipmentResourcesIdForPackage.contains(resourceId) && euipResource.getParent() != null) {
					throw new UnprocessableEntityException("EquipmentResource With id ( " + resourceId
							+ " ) already assigned to another package Id ( " + euipResource.getParent().getId() + " )");
				}
				euipResource.setParent(equipmentResource);
				return euipResource;
			}).collect(Collectors.toList());

			logger.info("Returning From If condition");
		}

		logger.info("Clear previous EquipmentResource which are assigned to the package : " + packageId);
		this.packageRepository.save(equipmentResources);
		logger.info("Cleared previous EquipmentResource which are assigned to the package : " + packageId);

		if (newlyEquipmentResources != null && !newlyEquipmentResources.isEmpty()) {
			logger.info("Inside If condition");
			logger.info("Assign new EquipmentResource to the package : " + packageId);
			this.packageRepository.save(newlyEquipmentResources);
			logger.info("Returning From If condition");
		}

		logger.info("Returning from PackageService, addEquipmentResourceToPackage");

	}

	/**
	 * Validate EquipmentResource id is valid or not.
	 * 
	 * @param packageId
	 * @return EquipmentResource
	 */
	public EquipmentResource validateEquipmentResource(Long packageId) {
		logger.info("Inside PackageService, validateEquipmentResource");
		logger.info("packageId : " + packageId);
		EquipmentResource equipmentResource = this.packageRepository.findOne(packageId);
		if (equipmentResource == null) {
			throw new UnprocessableEntityException("please enter a valid EquipmentResource id " + packageId);
		}
		logger.info("Returning from PackageService, validateEquipmentResource : found " + equipmentResource);
		return equipmentResource;
	}

	private EquipmentResource getTopMostParent(EquipmentResource equipmentResource) {
		if (equipmentResource == null) {
			return null;
		}
		EquipmentResource eq = equipmentResource.getParent();
		while (eq != null) {
			if (eq.getParent() == null) {
				break;
			}
			eq = eq.getParent();
		}
		return eq;
	}

}
