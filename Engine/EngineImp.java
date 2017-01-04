package engine;

import java.io.File;

import engine.lang.exceptions.EngineException;

public interface EngineImp
{
	public void run(File[] args) throws EngineException;
}
