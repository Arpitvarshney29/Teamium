package com.teamium.utils;

import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.functions.RightFunction;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.suppliers.SupplyFunction;

public class FunctionUtil {

	public FunctionUtil() {
	}

	public static String getFunctionType(Function function) {
		if (function.isFolder()) {
			return TeamiumConstants.FOLDER_FUNCTION_TYPE;
		}
		if (function instanceof StaffFunction) {
			return TeamiumConstants.STAFF_FUNCTION_TYPE;
		}
		if (function instanceof EquipmentFunction) {
			return TeamiumConstants.EQUIPMENT_FUNCTION_TYPE;
		}
		if (function instanceof ProcessFunction) {
			// process function is now service function
			return TeamiumConstants.PROCESS_FUNCTION_TYPE;
		}
		if (function instanceof SupplyFunction) {
			// supply function is now expenses function
			return TeamiumConstants.SUPPLY_FUNCTION_TYPE;
		}
		if (function instanceof RightFunction) {
			return TeamiumConstants.RIGHT_FUNCTION_TYPE;
		}
		return TeamiumConstants.DEFAULT_FUNCTION_TYPE;
	}
}
