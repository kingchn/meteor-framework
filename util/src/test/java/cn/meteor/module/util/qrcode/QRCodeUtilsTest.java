package cn.meteor.module.util.qrcode;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.zxing.NotFoundException;

public class QRCodeUtilsTest {

	private  static final Logger logger = LogManager.getLogger(QRCodeUtilsTest.class);

	@Test
	public void testDecodeQRCodeBase64StringToString() {
		String qrCodeBase64String ="Qk3CAwAAAAAAAD4AAAAoAAAASwAAAEsAAAABAAEAAAAAAIQDAAAAAAAAAAAAAAAAAAACAAAAAAAA///////////////////gAAAAAwM/A8/z/8AgAAAAAwM/A8/z/8AgAAA/8/DAAA/z/8DgAAA/8/DAAA/z/8DgAAAwM8A8zMA8M/PgAAAwM8A8zMA8M/PgAAAwM/D/8PA88/MgAAAwM/D/8PA88/MgAAAwMwAPPDzzADMgAAAwMwAPPDzzADMgAAA/8/AD/wzwPwPgAAA/8/AD/wzwPwPgAAAAAwPA88D/MzDgAAAAAwPA88D/MzDgAAD//wPAzzAMPzzgAAD//wPAzzAMPzzgAADw88zAAM/zAAAgAADw88zAAM/zAAAgAAAAz8Mzz8P/D8PgAAAAz8Mzz8P/D8PgAAADAMPDDwzwzwzgAAADAMPDDwzwzwzgAADwPPz/PAw8zAzgAADwPPz/PAw8zAzgAADP8MwD8/MzM8AgAADP8MwD8/MzM8AgAAAzPzMwA/Azz8PgAAAzPzMwA/Azz8PgAAA/MDPADzwMA/DgAAA/MDPADzwMA/DgAAAAzwww/wwAzA/gAAAAzwww/wwAzA/gAADwA///AwzwM8MgAADwA///AwzwM8MgAAAwzDzMDAAwDwDgAAAwzDzMDAAwDwDgAADw88P8/MD8zMDgAADw88P8/MD8zMDgAAA8z//D8PwMzDzgAAA8z//D8PwMzDzgAAAz8ADPPzzzM8DgAAAz8ADPPzzzM8DgAAAMz8/8/AzwPMDgAAAMz8/8/AzwPMDgAADDMDAA88z8D8DgAADDMDAA88z8D8DgAAAwPPwwzzAMzADgAAAwPPwwzzAMzADgAADMAw8wAM/zM8MgAADMAw8wAM/zM8MgAAAwPPwPz8wwDwPgAAAwPPwPz8wwDwPgAADMwAz/AwD8/MDgAADMwAz/AwD8/MDgAADzDAA/DAPMzDPgAADzDAA/DAPMzDPgAAA/MAAz8M8zAA8gAAA/MAAz8M8zAA8gAAD//zMwwAz8P//gAAD//zMwwAz8P//gAAAAAzMzMzMzMAAgAAAAAzMzMzMzMAAgAAA/8zwP8DMP8/8gAAA/8zwP8DMP8/8gAAAwMw888zAM8wMgAAAwMw888zAM8wMgAAAwMwz8/DAM8wMgAAAwMwz8/DAM8wMgAAAwM8P8zA//MwMgAAAwM8P8zA//MwMgAAA/88MAMz8z8/8gAAA/88MAMz8z8/8gAAAAAzzA8M/M8AAgAAAAAzzA8M/M8AAgAAA=";
		try {
			String content = QRCodeUtils.decodeQRCodeBase64StringToString(qrCodeBase64String);
			logger.info(content);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
