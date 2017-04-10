package test.IoTaBase;

import static org.junit.Assert.*;

import org.junit.Test;

import IoTaBase.IoTaUtil;
import IoTaBase.definitions.TemperatureDef;

public class TempDefTest {

	@Test
	public void test() {
		TemperatureDef tdef = new TemperatureDef();
		System.out.println(tdef.getInsertUpdate(1, 2));
	}
	
	@Test
	public void testFormatter(){
		System.out.println(IoTaUtil.time2DATETIME(System.currentTimeMillis()));
	}

}
