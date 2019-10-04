package cn.meteor.module.util.cert;

/**
 * 证书持有者（主体）
 * 该域描述了与主体公钥域中的公钥相对应的实体。
 * 主体名称必须包含一个X.500的甄别名称（DN），
 * 一个CA认证每个主体实体的甄别名称必须是唯一的，
 * 一个CA可以为同一个主体实体以相同的甄别名称签发多个证书，
 * 主体DN的格式与证书签发者DN相同。
 * @author shenjc
 *
 */
public class Subject {

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
	
	public String getSubjectString() {
//		String subjectString = "L=" + this.getL() + ", ST=" + this.getST() + ", O=" + this.getO() + ", OU=" + this.getOU() + ", CN=" + this.getCN() + ", C=" + this.getC();
		String subjectString = "CN=" + this.getCN() + ", OU=" + this.getOU() + ", O=" + this.getO() + ", L=" + this.getL() + ", ST=" + this.getST() + ", C=" + this.getC();
		return subjectString;
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
