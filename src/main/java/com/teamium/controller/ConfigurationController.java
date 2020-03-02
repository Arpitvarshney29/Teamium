package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.Category;
import com.teamium.domain.DocumentType;
import com.teamium.domain.EditionTemplateType;
import com.teamium.domain.Format;
import com.teamium.domain.FunctionKeyword;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.Skill;
import com.teamium.domain.prod.projects.order.BookingOrderForm;
import com.teamium.dto.ChannelDTO;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.DirectorDTO;
import com.teamium.dto.GroupDTO;
import com.teamium.service.BookingOrderFormService;
import com.teamium.service.CategoryService;
import com.teamium.service.ChannelService;
import com.teamium.service.CurrencyService;
import com.teamium.service.DirectorService;
import com.teamium.service.DocumentTypeService;
import com.teamium.service.EditionTemplateTypeService;
import com.teamium.service.FormatService;
import com.teamium.service.FunctionKeywordService;
import com.teamium.service.GroupService;
import com.teamium.service.MilestoneTypeService;
import com.teamium.service.SkillService;

/**
 * Controller to handle all request related to configuration.
 * 
 * @author Teamium
 *
 */
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

	private EditionTemplateTypeService editionTemplateTypeService;
	private GroupService groupService;
	private FormatService formatService;
	private CategoryService categoryService;
	private MilestoneTypeService milestoneTypeService;
	private ChannelService channelService;
	private CurrencyService currencyService;
	private DocumentTypeService documentTypeService;
	private BookingOrderFormService bookingOrderFormService;
	private SkillService skillService;
	private FunctionKeywordService functionKeywordService;
	private DirectorService directorService;

	@Autowired
	public ConfigurationController(EditionTemplateTypeService editionTemplateTypeService, GroupService groupService,
			FormatService formatService, CategoryService categoryService, MilestoneTypeService milestoneTypeService,
			ChannelService channelService, CurrencyService currencyService, DocumentTypeService documentTypeService,
			BookingOrderFormService bookingOrderFormService, SkillService skillService,
			FunctionKeywordService functionKeywordService, DirectorService directorService) {
		this.editionTemplateTypeService = editionTemplateTypeService;
		this.groupService = groupService;
		this.formatService = formatService;
		this.categoryService = categoryService;
		this.milestoneTypeService = milestoneTypeService;
		this.channelService = channelService;
		this.currencyService = currencyService;
		this.documentTypeService = documentTypeService;
		this.bookingOrderFormService = bookingOrderFormService;
		this.skillService = skillService;
		this.functionKeywordService = functionKeywordService;
		this.directorService = directorService;
	}

	/**
	 * To get save or update EditionTemplateType.
	 * 
	 * Service URL: /configuration/edition-template method: POST.
	 * 
	 * @param EditionTemplateType
	 * @return
	 */
	@PostMapping("/edition-template")
	EditionTemplateType saveOrUpdateDigitalSignatureEnvelope(@RequestBody EditionTemplateType editionTemplateType) {
		return editionTemplateTypeService.saveOrUpdate(editionTemplateType);
	}

	/**
	 * To get list of EditionTemplateType.
	 * 
	 * Service URL: /configuration/edition-template method: GET.
	 * 
	 * @return List<EditionTemplateType>
	 */
	@GetMapping("/edition-template")
	List<EditionTemplateType> getDigitalSignatureEnvelopes() {
		return editionTemplateTypeService.findAllEditionTemplateTypes();
	}

	/**
	 * To delete EditionTemplateType by id.
	 * 
	 * Service URL: /configuration/edition-template/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/edition-template/{id}")
	void deleteDigitalSignatureEnvelopes(@PathVariable("id") long id) {
		editionTemplateTypeService.deleteEditionTemplateTypeById(id);
	}

	/**
	 * To get EditionTemplateType by id.
	 * 
	 * Service URL: /configuration/edition-template/{id} method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/edition-template/{id}")
	EditionTemplateType getDigitalSignatureEnvelopeById(@PathVariable("id") long id) {
		return editionTemplateTypeService.findById(id);
	}

	/**
	 * To get save or update GroupDTO.
	 * 
	 * Service URL: /configuration/group method: POST.
	 * 
	 * @param GroupDTO
	 * @return
	 */
	@PostMapping("/group")
	GroupDTO saveOrGroup(@RequestBody GroupDTO groupDTO) {
		return groupService.saveOrUpdate(groupDTO);
	}

	/**
	 * To get list of GroupDTO.
	 * 
	 * Service URL: /configuration/group method: GET.
	 * 
	 * @return List<EditionTemplateType>
	 */
	@GetMapping("/group")
	List<GroupDTO> getGroups() {
		return groupService.getAllGroups();
	}

	/**
	 * To delete Group by id.
	 * 
	 * Service URL: /configuration/group/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/group/{id}")
	void deleteGroup(@PathVariable("id") long id) {
		groupService.deleteGroupById(id);
	}

	/**
	 * To get GroupDTO by id.
	 * 
	 * Service URL: /configuration/group/{id} method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/group/{id}")
	GroupDTO getGroupDTOById(@PathVariable("id") long id) {
		return groupService.findGroupDTOById(id);
	}

	/**
	 * 
	 * /** To add staff member to group.
	 * 
	 * Service URL: /configuration/group/add-staff/{groupId}/{staffMemberId} method:
	 * GET
	 * 
	 * @param groupId
	 * @param staffMemberId
	 * @return
	 */
	@PostMapping("/group/add-staff/{groupId}/{staffMemberId}")
	GroupDTO addMemberToGroup(@PathVariable("groupId") long groupId,
			@PathVariable("staffMemberId") long staffMemberId) {
		return groupService.addStaffMemberToGroup(groupId, staffMemberId);
	}

	/**
	 * To remove staff member to group.
	 * 
	 * Service URL: /configuration/group/remove-staff/{groupId}/{staffMemberId}
	 * method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/group/remove-staff/{groupId}/{staffMemberId}")
	GroupDTO removeMemberFromGroup(@PathVariable("groupId") long groupId,
			@PathVariable("staffMemberId") long staffMemberId) {
		return groupService.removeStaffMemberFromGroup(groupId, staffMemberId);
	}

	/**
	 * To get save or update ChannelDTO.
	 * 
	 * Service URL: /configuration/channel method: POST.
	 * 
	 * @param ChannelDTO
	 * @return
	 */
	@PostMapping("/channel")
	ChannelDTO saveOrUpdateChannel(@RequestBody ChannelDTO channelDTO) {
		return channelService.saveOrUpdateChannel(channelDTO);
	}

	/**
	 * To get list of ChannelDTO.
	 * 
	 * Service URL: /configuration/channel method: GET.
	 * 
	 * @return List<ChannelDTO>
	 */
	@GetMapping("/channel")
	List<ChannelDTO> getChannels() {
		return channelService.getChannels();
	}

	/**
	 * To delete channel by id.
	 * 
	 * Service URL: /configuration/channel/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/channel/{id}")
	void deleteChannel(@PathVariable("id") long id) {
		channelService.deleteChannel(id);
	}

	/**
	 * To get CompanyDTO by id.
	 * 
	 * Service URL: /configuration/channel/{id} method: GET.
	 * 
	 * @param id
	 * @return ChannelDTO
	 */
	@GetMapping("/channel/{id}")
	CompanyDTO getCompanyById(@PathVariable("id") long id) {
		return channelService.getChannel(id);
	}

	/**
	 * To save or update a format
	 * 
	 * Service URL:/configuration/format method: POST.
	 * 
	 * @return format object
	 */
	@RequestMapping(value = "/format", method = RequestMethod.POST)
	Format saveOrUpdateFormat(@RequestBody Format format) {
		return formatService.saveOrUpdateFormat(format);
	}

	/**
	 * To get list of all Formats
	 * 
	 * Service URL:/configuration/format method: GET.
	 * 
	 * @return list of formats
	 */
	@RequestMapping(value = "/format", method = RequestMethod.GET)
	List<Format> getAllFormats() {
		return formatService.getAllFormats();
	}

	/**
	 * To get list of all Formats name
	 * 
	 * Service URL:/configuration/format/name method: GET.
	 * 
	 * @return list of formats name
	 */
	@RequestMapping(value = "/format/name", method = RequestMethod.GET)
	List<String> getAllFormatsName() {
		return formatService.getAllFormatsName();
	}

	/**
	 * To get format by id.
	 * 
	 * Service URL:/configuration/format/{formatId} method: GET.
	 * 
	 * @param formatId
	 * 
	 * @return format object.
	 */
	@RequestMapping(value = "/format/{formatId}", method = RequestMethod.GET)
	Format getFormatById(@PathVariable Long formatId) {
		return formatService.getFormatById(formatId);
	}

	/**
	 * To delete format by id.
	 * 
	 * Service URL:/configuration/format/{formatId} method: DELETE.
	 * 
	 * @param formatId
	 */
	@RequestMapping(value = "/format/{formatId}", method = RequestMethod.DELETE)
	void deleteFormat(@PathVariable Long formatId) {
		formatService.deleteFormat(formatId);
	}

	/**
	 * To save or update a category
	 * 
	 * Service URL:/configuration/category method: POST.
	 * 
	 * @return category object
	 */
	@RequestMapping(value = "/category", method = RequestMethod.POST)
	Category saveOrUpdateCategory(@RequestBody Category category) {
		return categoryService.saveOrUpdateCategory(category);
	}

	/**
	 * To get list of all Category
	 * 
	 * Service URL:/configuration/category method: GET.
	 * 
	 * @return list of category
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	List<Category> getAllCategory() {
		return categoryService.getAllCategory();
	}

	/**
	 * To get list of all category name
	 * 
	 * Service URL:/configuration/category/name method: GET.
	 * 
	 * @return list of category name
	 */
	@RequestMapping(value = "/category/name" + "", method = RequestMethod.GET)
	List<String> getAllCategoryName() {
		return categoryService.getAllCategoryName();
	}

	/**
	 * To get category by id.
	 * 
	 * Service URL:/configuration/category/{categoryId} method: GET.
	 * 
	 * @param categoryId
	 * 
	 * @return category object.
	 */
	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	Category getCategoryById(@PathVariable Long categoryId) {
		return categoryService.getCategoryById(categoryId);
	}

	/**
	 * To delete category by id.
	 * 
	 * Service URL:/configuration/category/{categoryId} method: DELETE.
	 * 
	 * @param categoryId
	 */
	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.DELETE)
	void deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
	}

	/**
	 * To save or update a milestone-type
	 * 
	 * Service URL:/configuration/milestone method: POST.
	 * 
	 * @return milestone object
	 */
	@RequestMapping(value = "/milestone", method = RequestMethod.POST)
	MilestoneType saveOrUpdateMilestoneType(@RequestBody MilestoneType milestoneType) {
		return milestoneTypeService.saveOrUpdateMilestoneType(milestoneType);
	}

	/**
	 * To get list of all milestone-type by discriminator
	 * 
	 * Service URL:/configuration/milestone/{discriminator} method: GET.
	 * 
	 * @return list of milestone-type
	 */
	@RequestMapping(value = "/milestone/{discriminator}", method = RequestMethod.GET)
	List<MilestoneType> getAllMilestoneType(@PathVariable String discriminator) {
		return milestoneTypeService.getAllMilestoneType(discriminator);
	}

	/**
	 * To get list of all milestone-type name by discriminator
	 * 
	 * Service URL:/configuration/milestone/name/{discriminator} method: GET.
	 * 
	 * @param discriminator
	 * 
	 * @return list of milestone name
	 */
	@RequestMapping(value = "/milestone/name/{discriminator}", method = RequestMethod.GET)
	List<String> getAllMilestoneTypeName(@PathVariable String discriminator) {
		return milestoneTypeService.getAllMilestoneTypeName(discriminator);
	}

	/**
	 * To get milestone-type by id and discriminator.
	 * 
	 * Service URL:/configuration/milestone/{milestoneId}/{discriminator} method:
	 * GET.
	 * 
	 * @param discriminator
	 * @param milestoneId
	 * 
	 * @return milestone-type object.
	 */
	@RequestMapping(value = "/milestone/{discriminator}/{milestoneId}", method = RequestMethod.GET)
	MilestoneType getMilestoneTypeByIdAndDiscriminator(@PathVariable String discriminator,
			@PathVariable Long milestoneId) {
		return milestoneTypeService.getMilestoneTypeByIdAndDiscriminator(discriminator, milestoneId);
	}

	/**
	 * To delete milestone-type by id and discriminator.
	 * 
	 * Service URL:/configuration/milestone/{milestoneId}/{discriminator} method:
	 * DELETE.
	 * 
	 * @param categoryId
	 * @param discriminator
	 */
	@RequestMapping(value = "/milestone/{discriminator}/{milestoneId}", method = RequestMethod.DELETE)
	void deleteMilestoneType(@PathVariable String discriminator, @PathVariable Long milestoneId) {
		milestoneTypeService.deleteMilestoneType(discriminator, milestoneId);
	}

	/**
	 * To save/update currency
	 * 
	 * Service URL: /configuration/currency method: POST.
	 * 
	 * @param CurrencyDTO the CurrencyDTO
	 * 
	 * @return the currency wrapper object
	 */
	@PostMapping("/currency")
	public CurrencyDTO saveOrUpdateCurrency(@RequestBody CurrencyDTO currencyDTO) {
		return currencyService.saveOrUpdateCurrency(currencyDTO);
	}

	/**
	 * To get currency by id
	 * 
	 * Service URL: /configuration/currency/{currencyId} method: GET.
	 * 
	 * @param countryId the countryId
	 * 
	 * @return the CurrencyDTO
	 */
	@GetMapping("/currency/{currencyId}")
	public CurrencyDTO findCurrencyById(@PathVariable long currencyId) {
		return currencyService.getCurrencyById(currencyId);
	}

	/**
	 * To get all currency
	 * 
	 * Service URL: /configuration/currency method: GET.
	 * 
	 * @return the list of Currency
	 */
	@GetMapping("/currency")
	public List<CurrencyDTO> getAllCurrency() {
		return currencyService.getAllCurrency();
	}

	/**
	 * To get currency by status
	 * 
	 * Service URL: /configuration/currency/status/{active} method: GET.
	 * 
	 * @param active
	 * 
	 * @return the list of Currency by status
	 */
	@GetMapping("/currency/status/{active}")
	public List<CurrencyDTO> getAllCurrencyByActive(@PathVariable boolean active) {
		return currencyService.getAllCurrencyByActive(active);
	}

	/**
	 * To get currency-codes by status
	 * 
	 * Service URL: /configuration/currency/code/status/{active} method: GET.
	 * 
	 * @param active
	 * 
	 * @return the list of Currency-codes by status
	 */
	@GetMapping("/currency/code/status/{active}")
	public List<String> getAllCurrencyCodeStringByActive(@PathVariable boolean active) {
		return currencyService.getAllCurrencyCodeStringByActive(active);
	}

	/**
	 * To update all currency-list
	 * 
	 * @param currencyDTOList
	 * 
	 * @return all currency list
	 */
	@PostMapping("/currency/list/update")
	public List<CurrencyDTO> updateCurrencyList(@RequestBody List<CurrencyDTO> currencyDTOList) {
		return currencyService.updateCurrencyList(currencyDTOList);
	}

	/**
	 * To save or update a document-type
	 * 
	 * Service URL:/configuration/document-type method: POST.
	 * 
	 * @return document-type object
	 */
	@RequestMapping(value = "/document-type", method = RequestMethod.POST)
	DocumentType saveOrUpdateDocumentType(@RequestBody DocumentType documentType) {
		return documentTypeService.saveOrUpdateDocumentType(documentType);
	}

	/**
	 * To get list of all Document-Type
	 * 
	 * Service URL:/configuration/document-type method: GET.
	 * 
	 * @return list of document-type
	 */
	@RequestMapping(value = "/document-type", method = RequestMethod.GET)
	List<DocumentType> getAllDocumentType() {
		return documentTypeService.getAllDocumentType();
	}

	/**
	 * To get list of all document-type name
	 * 
	 * Service URL:/configuration/document-type/name method: GET.
	 * 
	 * @return list of document-type name
	 */
	@RequestMapping(value = "/document-type/name", method = RequestMethod.GET)
	List<String> getAllDocumentTypeName() {
		return documentTypeService.getAllDocumentTypeName();
	}

	/**
	 * To get document-type by id.
	 * 
	 * Service URL:/configuration/document-type/{documentTypeId} method: GET.
	 * 
	 * @param documentType
	 * 
	 * @return document-type object.
	 */
	@RequestMapping(value = "/document-type/{documentTypeId}", method = RequestMethod.GET)
	DocumentType getDocumentTypeById(@PathVariable Long documentTypeId) {
		return documentTypeService.getDocumentTypeById(documentTypeId);
	}

	/**
	 * To delete category by id.
	 * 
	 * Service URL:/configuration/document-type/{documentTypeId} method: DELETE.
	 * 
	 * @param documentTypeId
	 */
	@RequestMapping(value = "/document-type/{documentTypeId}", method = RequestMethod.DELETE)
	void deleteDocumentType(@PathVariable Long documentTypeId) {
		documentTypeService.deleteDocumentType(documentTypeId);
	}

	/**
	 * To get save or update BookingOrderForm.
	 * 
	 * Service URL: /configuration/booking-order-form method: POST.
	 * 
	 * @param BookingOrderForm
	 * @return
	 */
	@PostMapping("/booking-order-form")
	BookingOrderForm saveOrUpdateBookingOrderForm(@RequestBody BookingOrderForm bookingOrderForm) {
		return bookingOrderFormService.saveOrUpdateBookingOrderForm(bookingOrderForm);
	}

	/**
	 * To get list of BookingOrderForm.
	 * 
	 * Service URL: /configuration/booking-order-form method: GET.
	 * 
	 * @return List<BookingOrderForm>
	 */
	@GetMapping("/booking-order-form")
	List<BookingOrderForm> getBookingOrderForm() {
		return bookingOrderFormService.findAll();
	}

	/**
	 * To delete BookingOrderForm by id.
	 * 
	 * Service URL: /configuration/booking-order-form/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/booking-order-form/{id}")
	void deleteBookingOrderForm(@PathVariable("id") long id) {
		bookingOrderFormService.deleteBookingOrderFormById(id);
	}

	/**
	 * To get EditionTemplateType by id.
	 * 
	 * Service URL: /configuration/booking-order-form/{id} method: GET.
	 * 
	 * @param id
	 * 
	 * @return bookingOrderForm object
	 */
	@GetMapping("/booking-order-form/{id}")
	BookingOrderForm getBookingOrderFormById(@PathVariable("id") long id) {
		return bookingOrderFormService.findById(id);
	}

	/**
	 * To add and update skills.
	 * 
	 * Service URL: /configuration/skill Method: POST.
	 * 
	 * @param skill
	 * 
	 * @return skill object
	 */
	@PostMapping("/skill")
	Skill addSkill(@RequestBody Skill skill) {
		return skillService.addSkill(skill);
	}

	/**
	 * To get skill by skillId.
	 * 
	 * Service URL: /configuration/skill/{skillId} Method: GET.
	 * 
	 * @param skillId
	 * 
	 * @return skill object
	 */
	@GetMapping("/skill/{skillId}")
	Skill getSkillById(@PathVariable Long skillId) {
		return skillService.getSkillById(skillId);
	}

	/**
	 * To get all skills.
	 * 
	 * Service URL: /configuration/skill Method: GET.
	 * 
	 * @return list of all skills
	 */
	@GetMapping("/skill")
	List<Skill> getSkills() {
		return skillService.getSkills();
	}

	/**
	 * To delete skill by skillId.
	 * 
	 * Service URL: /configuration/skill/{skillId} Method: DELETE.
	 * 
	 * @param skillId
	 */
	@DeleteMapping("/skill/{skillId}")
	void removeSkillById(@PathVariable Long skillId) {
		skillService.removeSkillById(skillId);
	}

	/**
	 * To add and update functionKeywords.
	 * 
	 * Service URL: /configuration/keyword Method: POST.
	 * 
	 * @param functionKeyword
	 * @return functionKeyword
	 */
	@PostMapping("/keyword")
	FunctionKeyword addFunctionKeyword(@RequestBody FunctionKeyword functionKeyword) {
		return functionKeywordService.addFunctionKeyword(functionKeyword);
	}

	/**
	 * To get all functionKeywords.
	 * 
	 * Service URL: /configuration/keyword Method: GET.
	 * 
	 * @return list of all functionKeywords
	 */
	@GetMapping("/keyword")
	List<FunctionKeyword> getFunctionKeywords() {
		return functionKeywordService.getFunctionKeywords();
	}

	/**
	 * To get functionKeyword by id.
	 * 
	 * Service URL: /configuration/keyword/{id} Method: GET.
	 * 
	 * @return functionKeyword
	 */
	@GetMapping("/keyword/{id}")
	FunctionKeyword getFunctionKeyword(@PathVariable Long id) {
		return functionKeywordService.getFunctionKeyword(id);
	}

	/**
	 * To delete functionKeyword by id.
	 * 
	 * Service URL: /configuration/keyword/{id} Method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/keyword/{id}")
	void removeFunctionKeywordById(@PathVariable Long id) {
		functionKeywordService.removeFunctionKeywordById(id);
	}

	/**
	 * To update Director.
	 * 
	 * Service URL: /configuration/director Method: POST.
	 * 
	 * 
	 * @return directorDTO
	 */
	@PostMapping("/director")
	DirectorDTO saveOrUpdateDirector(@RequestBody DirectorDTO directorDTO) {
		return directorService.saveOrUpdateDirector(directorDTO);
	}

	/**
	 * To get All Director.
	 * 
	 * Service URL: /configuration/director Method: GET.
	 * 
	 * 
	 * @return directorDTO
	 */
	@GetMapping("/director")
	List<DirectorDTO> getDirectors() {
		return directorService.getDirectors();
	}

	/**
	 * To get Director By id .
	 * 
	 * Service URL: /configuration/director Method: GET.
	 * 
	 * 
	 * @return List<DirectorDTO>
	 */
	@GetMapping("/director/{directorId}")
	DirectorDTO getDirector(@PathVariable Long directorId) {
		return directorService.getDirector(directorId);
	}

	/**
	 * To delete Director by id.
	 * 
	 * Service URL: /configuration/keyword/{id} Method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/director/{directorId}")
	void removeDirectorById(@PathVariable Long directorId) {
		directorService.removeDirectorById(directorId);
	}
}
