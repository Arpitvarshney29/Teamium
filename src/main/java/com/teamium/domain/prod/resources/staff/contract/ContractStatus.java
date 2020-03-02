package com.teamium.domain.prod.resources.staff.contract;

public enum ContractStatus {

	NOT_MADE("not_made"),
	MADE("made"),
	PRINTED("printed"),
	SIGNED("signed"),
	DUE("due_sent");
	
	private String value;
	
	ContractStatus(String s){
		this.value = s;
	}
	
	public String getValue()
	{
		return this.value;
	}
}
