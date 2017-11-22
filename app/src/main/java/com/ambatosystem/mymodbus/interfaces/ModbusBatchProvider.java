package com.ambatosystem.mymodbus.interfaces;

import com.serotonin.modbus4j.BatchRead;

/****************************************************************
 *
 *
 *
 *
 ****************************************************************/
public interface ModbusBatchProvider {

	public BatchRead getBatch();
	
}
