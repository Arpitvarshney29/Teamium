package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.DigitalSignatureEnvelopeDTO;
import com.teamium.dto.SignatureHistoryDTO;
import com.teamium.service.DigitalSignatureService;

@RestController
@RequestMapping("digital-signature")
public class DigitalSignatureController {

	private DigitalSignatureService digitalSignatureService;

	@Autowired
	public DigitalSignatureController(DigitalSignatureService digitalSignatureService) {
		this.digitalSignatureService = digitalSignatureService;
	}

	/**
	 * To get save or update DigitalSignatureEnvelope.
	 * 
	 * Service URL: /digital-signature/envelope method: POST.
	 * 
	 * @param digitalSignatureEnvelopeDTO
	 * @return
	 */
	@PostMapping("/envelope")
	DigitalSignatureEnvelopeDTO saveOrUpdateDigitalSignatureEnvelope(
			@RequestBody DigitalSignatureEnvelopeDTO digitalSignatureEnvelopeDTO) {
		return digitalSignatureService.saveOrUpdateDigitalSignatureEnvelope(digitalSignatureEnvelopeDTO);
	}

	/**
	 * To get list of DigitalSignatureEnvelope.
	 * 
	 * Service URL: /digital-signature/envelope method: GET.
	 * 
	 * @return List<DigitalSignatureEnvelopeDTO>
	 */
	@GetMapping("/envelope")
	List<DigitalSignatureEnvelopeDTO> getDigitalSignatureEnvelopes() {
		return digitalSignatureService.getAllDigitalSignatureEnvelopes();
	}

	/**
	 * To delete DigitalSignatureEnvelope by id.
	 * 
	 * Service URL: /digital-signature/envelope/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/envelope/{id}")
	void deleteDigitalSignatureEnvelopes(@PathVariable("id") long id) {
		digitalSignatureService.deleteDigitalSignatureEnvelopeById(id);
	}

	/**
	 * To get DigitalSignatureEnvelope by id.
	 * 
	 * Service URL: /digital-signature/envelope/{id} method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/envelope/{id}")
	DigitalSignatureEnvelopeDTO getDigitalSignatureEnvelopeById(@PathVariable("id") long id) {
		return digitalSignatureService.findDigitalSignatureEnvelopeDTOById(id);
	}

	/**
	 * To get the list of SignatureRecipient for a record by
	 * templateName(EditionTemplateType) and record id.
	 * 
	 * Service URL: /digital-signature/get-all-recipient/{templateName}/{recordId}
	 * method: GET.
	 * 
	 * @param templateName
	 * @param recordId
	 * @return List<SignatureHistoryDTO>
	 */
	@GetMapping("/get-all-recipient/{templateName}/{recordId}")
	List<SignatureHistoryDTO> getAllSignatureRecipient(
			@PathVariable(value = "templateName", required = true) String templateName,
			@PathVariable(value = "recordId", required = true) Long recordId) {
		return digitalSignatureService.getAllSignatureRecipient(templateName, recordId);
	}

	/**
	 * To sign a record template.
	 * 
	 * Service URL: /digital-signature/sign
	 * method: POST.
	 * 
	 * @param signatureHistoryDTO
	 * @return signatureHistoryDTO
	 */
	@PostMapping(value = "/sign")
	SignatureHistoryDTO signEdition(@RequestBody SignatureHistoryDTO signatureHistoryDTO) {
		return digitalSignatureService.signEdition(signatureHistoryDTO);
	}

}
