package cn.meteor.module.util.security;

/**
 * 预防XSS工具类
 * <p>XSS (Cross Site Scripting) 跨站脚本攻击</p>
 * 
 * <p>
 * <b>1. script方式---replaceDangerousScriptString</b><br/>
 *  replace(str,"<","&#x3C;") &#ascii方式字符放入script块中不解释<br/>
 *  replace(str,"<","&#x3E;") <br/>
 *  注：script块为服务器端语句，不进行ascii解释，所以过滤"<"、">"基本可以防范跨站。
 *      “&#xxx;”是unicode的HTML表示方式，
 *       格式为：&#+unicode编码的十进制数;
 * </p> 
 * @author shenjc
 *
 */
public class XSSPreventionUtils {

	/**
	 * 预防XSS,替换危险script字符串
	 * @param originalString 原始字符串
	 * @return 安全字符串
	 */
	public static String replaceDangerousScriptString(String originalString) {
		/*		
		Escape the following characters with HTML entity encoding to prevent switching into 	any execution context, 
		such as script, style, or event handlers. Using hex entities is recommended in the spec. 
		In addition to the 5 characters significant in XML (&, <, >, ", '), 
		the forward slash is included as it helps to end an HTML entity.
				  & --> &amp;
				 < --> &lt;
				 > --> &gt;
				 " --> &quot;
				 ' --> &#x27;     &apos; is not recommended
				 / --> &#x2F;     forward slash is included as it helps end an HTML entity				 
		*/

		if(originalString!=null) {
			originalString = originalString.replace("&", "&#x26;");
			originalString = originalString.replace("<", "&#x3C;");
			originalString = originalString.replace(">", "&#x3E;");
			
			originalString = originalString.replace("\"", "&#x22;");
			originalString = originalString.replace("\'", "&#x27;");
			originalString = originalString.replace("/", "&#x2F;");

//			originalString = originalString.replace(" ", "&nbsp;");
//			originalString = originalString.replace("/**/", "&nbsp;");
		}
		return originalString;
	}
}
