package cn.meteor.module.util.cert;

/**
 * 证书签发者
 * 这个域标识了签发证书的实体，它必须包含一个非空的甄别名称（DN-distinguished name）.
 * 这个域被定义成为X.501的Name类型，
 * 通常DN的格式如下：
 * C=国家，S=省（市），L=区（县、市），O=组织机构，OU=组织单位，CN=通用名称。
 * @author shenjc
 *
 */
public class Issuer {
	
	/**
	 * commonName 通用名称 您的名字与姓氏
	 */
	private String CN;
	
	/**
	 * organizationUnit 组织单位名称
	 */
	private String OU;
	
	/**
	 * organizationName 组织名称
	 */
	private String O;
	
	/**
	 * localityName 地址 所在城市或者区域名称
	 */
	private String L;
	
	/**
	 * stateName 州名 省/市/自治区名称
	 */
	private String ST;
	
	/**
	 * country 双字母国家/地区代码
	 */
	private String C;
	
	public String getIssuerString() {
//		String issuerString = "O=" + this.getO() + ", OU=" + this.getOU() + ", CN=" + this.getCN() +", C=" + this.getC();
		String issuerString = "CN=" + this.getCN() + ", OU=" + this.getOU() + ", O=" + this.getO() +", C=" + this.getC();
		return issuerString;
	}

	public String getCN() {
		return CN;
	}

	public void setCN(String cN) {
		CN = cN;
	}

	public String getOU() {
		return OU;
	}

	public void setOU(String oU) {
		OU = oU;
	}

	public String getO() {
		return O;
	}

	public void setO(String o) {
		O = o;
	}

	public String getL() {
		return L;
	}

	public void setL(String l) {
		L = l;
	}

	public String getST() {
		return ST;
	}

	public void setST(String sT) {
		ST = sT;
	}

	public String getC() {
		return C;
	}

	public void setC(String c) {
		C = c;
	}
	

}
