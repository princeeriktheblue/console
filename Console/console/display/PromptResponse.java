package console.display;

import lib.ESystem;

public class PromptResponse 
{
	public PromptResponse(int state, int response)
	{
		this.state = state;
		this.response = response;
		nonStateResponse = "" + response;
	}
	
	public PromptResponse(int state, String nonStateResponse)
	{
		this.state = state;
		this.response = ESystem.NONBINARY_ANSWER;
		this.nonStateResponse = nonStateResponse;
	}
	
	public int getState()
	{
		return state;
	}
	
	public int getResponse()
	{
		return response;
	}
	
	public String getNonStateResponse()
	{
		return nonStateResponse;
	}

	private final int state;
	private final int response;
	private final String nonStateResponse;
}
