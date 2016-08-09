package com.client.core.formtrigger.controller.placementchangerequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bullhornsdk.data.api.BullhornData;
import com.bullhornsdk.data.model.entity.core.standard.PlacementChangeRequest;
import com.client.core.base.tools.web.MediaTypes;
import com.client.core.base.workflow.node.Node;
import com.client.core.formtrigger.controller.AbstractFormTriggerController;
import com.client.core.formtrigger.model.form.impl.FormPlacementChangeRequestDto;
import com.client.core.formtrigger.workflow.traversing.impl.PlacementChangeRequestValidationTraverser;

/**
 * Entry point for Placement change request formtrigger Validations.
 * 
 * The validate method instantiates a Traverser that will get passed through the workflow. The workflow is wired in formtrigger-workflow.xml
 * 
 * Actual logic is handled in Service class (see Service folder: com/client/core/service/formtrigger).
 */

@Controller
@RequestMapping("/formtrigger/placementchangerequest/*")
public class PlacementChangeRequestFormTriggerController extends
		AbstractFormTriggerController<PlacementChangeRequest, PlacementChangeRequestValidationTraverser> {

	@Autowired
	public PlacementChangeRequestFormTriggerController(
			@Qualifier("placementChangeRequestValidationWorkFlow") Node<PlacementChangeRequestValidationTraverser> placementChangeRequestValidationWorkflow,
			BullhornData bullhornData) {
		super(bullhornData, PlacementChangeRequest.class, placementChangeRequestValidationWorkflow);
	}

	/**
	 * Same entry point for both add and edit.
	 * 
	 * @param formPlacementChangeRequestDto
	 *            contains all the relevant data from the form
	 * @param updatingUserID
	 *            id of corporate user who saved the form
	 * @return the json parsed form response message
	 */
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, produces = { MediaTypes.JSON })
	@ResponseBody
	public String addEntity(@ModelAttribute FormPlacementChangeRequestDto formPlacementChangeRequestDto,
			@RequestParam("ft.userId") Integer updatingUserID) {
		log.info("---------------------------- Starting Placement Change Request Validation Process----------------------------------------");

		PlacementChangeRequestValidationTraverser traverser = new PlacementChangeRequestValidationTraverser(formPlacementChangeRequestDto,
				updatingUserID, isEdit(formPlacementChangeRequestDto), bullhornData);

		return handleRequest(traverser);
	}

	/**
	 * Checks is the form post is adding or editing a placement change request
	 * 
	 * @param formPlacementChangeRequestDto
	 * @return false if placementChangeRequestID is null or -1, true otherwise.
	 */
	private boolean isEdit(FormPlacementChangeRequestDto formPlacementChangeRequestDto) {
		Integer placementChangeRequestID = formPlacementChangeRequestDto.getPlacementChangeRequestID();

		if (placementChangeRequestID == null || placementChangeRequestID == -1) {
			return false;
		}

		return true;
	}

}