package cn.meteor.module.util.security;

public class RSAUtilsTestConstants {
	
			
	/**
	 * 提供给客户端的请求加密公钥(Auth)
	 */
	public static final String API_REQ_PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdjM9QwNGOqkWFWLIV49zIBGML" +
			"5Sim7qqCadX4Dt4tOlxwnKHmITrD+I4ThOXUlHHmTEFEBbO/CBYmhyzrpkGQhhua" +
			"bNo8N6Ctvn3HMheTJjdCABrOWNOjujUiUzFTbQbGtq4i2C9NFxUSZvhMaJ6XvykF" +
			"7o8Ij0hDWH5S333Q6QIDAQAB";
	
	/**
	 * 服务端用于请求参数的解密私钥(Auth)
	 */
	public static final String API_REQ_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAN2Mz1DA0Y6qRYVY" +
			"shXj3MgEYwvlKKbuqoJp1fgO3i06XHCcoeYhOsP4jhOE5dSUceZMQUQFs78IFiaH" +
			"LOumQZCGG5ps2jw3oK2+fccyF5MmN0IAGs5Y06O6NSJTMVNtBsa2riLYL00XFRJm" +
			"+Exonpe/KQXujwiPSENYflLffdDpAgMBAAECgYEAtNkxjhCnvxZdA5nAXUAQxDFY" +
			"jKg2Q/YEt8ofAItab375YG9rdQhhCQGHMIXGen3mcACFMjqZXHIEatTjJUUktIh0" +
			"C8QisDtHYLynGkmjGE86Rh5/d8DxaiwSNnYPlBZr7EK0sv0c7ZrWzm6M3wPOVLX1" +
			"dQE8rGKXkzYSRWxWBAECQQD7YL1lIIh06Hd8MRGI7fDrCAdeEuwe13/xRJRBzmdp" +
			"DIKjXVdrA9eWHLEUbT9hrAWVL8oBSyISzrur4yW8gZaBAkEA4Z+q2vOqOnqO0N1R" +
			"PMc4HEmm4fR7mnwadL1+O2RHvsgqSga72KrfyC7udDr3R4iFZ6kHEJ2rfJ+OFpgC" +
			"x2gWaQJBAPWd4ZDFB+/LEKyNOUfkzeioEKLPtFyyTbXNP1tJNOuEqMS8uGS8/VIU" +
			"OMXvtOVXcqEBb8xxwBpGW0MpD78vk4ECQQDUE6Mjv9oIsd7AUOfle1UwscJdrdLp" +
			"OTIc/WlEJlSR7dBiWMYFOUiz//k/45U/9gF/mI/9fFcPA2MptaUXDq3RAkEAglAB" +
			"BVdKnxqxX3riNunjFnfnbwx073pZOLS0jX4iAPAiMBQt6Lf6ZU+Xpp4/vLVYO72C" +
			"HuOowW3dwUALrs4Zjw==";
	
	
	/**
	 * 服务器对返回结果进行加密的公钥(Auth)
	 */
	public static final String API_RESP_PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDa8pNZU7y+3ao0lmLXIZzc6W58" +
			"z9BGuu64FiZY08ZK4x/AGVipjz52iDNDbxOLKMa4j2YNXJU8jFy5rZxiWmuyDL+/" +
			"cyeLWbg6njHOXKaHB942H3ByOMl5GJ6x3WzEQclV/Wj0TWR8MLIlygNzNySNLcQj" +
			"yK/0zYX11ajFeTB51wIDAQAB";
	
	/**
	 * 提供给客户端用于返回结果的解密私钥(Auth)
	 */
	public static final String API_RESP_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANryk1lTvL7dqjSW" +
			"YtchnNzpbnzP0Ea67rgWJljTxkrjH8AZWKmPPnaIM0NvE4soxriPZg1clTyMXLmt" +
			"nGJaa7IMv79zJ4tZuDqeMc5cpocH3jYfcHI4yXkYnrHdbMRByVX9aPRNZHwwsiXK" +
			"A3M3JI0txCPIr/TNhfXVqMV5MHnXAgMBAAECgYBOAUCYAapsQeMjCzU5ukL9vbjc" +
			"hNmKaY0lTtborMKn6ZVlRmJ9PoidiFbjPo6y9JsgJA7S0dplkQh0KrNdoyNcyeIM" +
			"OYpPr1Ph5JfaZPPEkTat+J7QJWcG6F3xcm8Q7ywyIFJaeq7duPkqglfxSeFcoZ4o" +
			"kegel0yuBowOpP+dkQJBAP60O8H0d1PHQUknIrj8xm7z2GS4Jzl56GXwPr1tGAKZ" +
			"OxOjrSKB5egqbML3H3MJ73y8NCL6eF+0N56eoFeVce8CQQDcD8RttbZtSxieUTw/" +
			"HB+wWp09rVTaBSYRbz5wQtLe4Mx7RP1Ylyv3biCZd+OEki86kQorLYxPsrM/mgY+" +
			"sL6ZAkB06z9TNWlhZ+IsNm7WBBMC86St92rqE2/++12RjvaqmrRnu4bKhF6JSJBM" +
			"nywsnq70z2APfnKrXAr8IUzxZ4S5AkBYMXV+pnmjGZqXMGVdwY6tpdMoucOs0K1u" +
			"BVBXu2A5dIxexspl68fyFM+50cN8CP4mkaQqo8l801hlH/xquXjZAkAejGQZUeJI" +
			"MjdoUf/5x7VPIS5Vqcy2s5TeJ8hcAPMEZ42V/IxZKjYYHrfGxDJKBU/ZaCikidqV" +
			"3tiSvwPYbxmE";
	
	
	/**
	 * 接口SESSION超时时间--20分钟
	 */
	public static final int API_SESSION_EXPIRE_TIME = 60*20;
	
	
	public static final String REQUEST_ATTRIBUTE_USER_SESSION_NAME = "requestAttributeUserSessionName";
	
	public static final String API_EXCEPTION_3DES_PUBLIC_KEY = "123456000000000000000000";
	
	public static final String API_EXCEPTION_3DES_PRIVATE_KEY = "123456000000000000000000";
	
	
	public static final String BASE64_STRING_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5rF5vyCH/SVSCbVDwk+3a/GK1"
			+ "y1bO1pWjV5ysyBvujMLDyKUogAcYMx637PQI4LJqDxwzv2tHhPgGEy3j8zKUydNC"
			+ "rxByIG2W2GfKlOvQV05woD0tDuSRbHrcRvUEx/Cf36RmlzSV/pOcSC167hpIOudC"
			+ "nxkSwAsDhb+iycqLpQIDAQAB";	
	public static final String BASE64_STRING_PRIVATE_KEY = "MIICXQIBAAKBgQC5rF5vyCH/SVSCbVDwk+3a/GK1y1bO1pWjV5ysyBvujMLDyKUo"
			+ "gAcYMx637PQI4LJqDxwzv2tHhPgGEy3j8zKUydNCrxByIG2W2GfKlOvQV05woD0t"
			+ "DuSRbHrcRvUEx/Cf36RmlzSV/pOcSC167hpIOudCnxkSwAsDhb+iycqLpQIDAQAB"
			+ "AoGBAKIKH0S4yYrNCXbmRkwfHecOemsjsXfD6EXsUSytW0seB1/sPM8SpJ5nBrkH"
			+ "j5Yr2ykaVMeIl+yLzBUpKdSVSx71TG/38Td/V1afni7+6fq3iQVocpdVN0C6Z1bS"
			+ "KQtbj1GgmWKKWKfXFk51hxAyW/66lLruNo61RXdhK2VeejRBAkEA91o1cd7ySSg3"
			+ "vRbANW73F7fmH3b+2yJ/lgtw832q3R7EBPNwPHIbhZcevM7pD4EwPjcaNt83ECoD"
			+ "EIoEs0p28QJBAMAqIrno0UcWy5gZ+sK0lr9WGf1lpj4c3AHgz6NVxmtyEsh3HEje"
			+ "WoQF8zZmmKqi4dNuedi/k5SutkMhIiHoJ/UCQBK6T8WZXbrQQrgcWt0w06CGGfRT"
			+ "7CPnTsWrhBfLcf5f7/N4Aw1wjkjlEjy1Zcv4uhBb165D5EVjCOxdptF0V+ECQF4w"
			+ "iQLs/h9FKhfsq//hun4geu68c0bAqIn3Im9h7LbfnBtMXr3M1zsdG4BPu4bv/Za/"
			+ "8NNv26umE0pGJE/QVj0CQQCo0qVibQpABIZ03k8del6Id6s8YsDgpX8exh94Bru7"
			+ "HQzquSePnplpnO2B/WXEf8s4zqH54L8A2t54yUpUSl9G";
	public static final String BASE64_STRING_PRIVATE_KEY_PKCS8 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALmsXm/IIf9JVIJt"
			+ "UPCT7dr8YrXLVs7WlaNXnKzIG+6MwsPIpSiABxgzHrfs9AjgsmoPHDO/a0eE+AYT"
			+ "LePzMpTJ00KvEHIgbZbYZ8qU69BXTnCgPS0O5JFsetxG9QTH8J/fpGaXNJX+k5xI"
			+ "LXruGkg650KfGRLACwOFv6LJyoulAgMBAAECgYEAogofRLjJis0JduZGTB8d5w56"
			+ "ayOxd8PoRexRLK1bSx4HX+w8zxKknmcGuQePlivbKRpUx4iX7IvMFSkp1JVLHvVM"
			+ "b/fxN39XVp+eLv7p+reJBWhyl1U3QLpnVtIpC1uPUaCZYopYp9cWTnWHEDJb/rqU"
			+ "uu42jrVFd2ErZV56NEECQQD3WjVx3vJJKDe9FsA1bvcXt+Yfdv7bIn+WC3Dzfard"
			+ "HsQE83A8chuFlx68zukPgTA+Nxo23zcQKgMQigSzSnbxAkEAwCoiuejRRxbLmBn6"
			+ "wrSWv1YZ/WWmPhzcAeDPo1XGa3ISyHccSN5ahAXzNmaYqqLh02552L+TlK62QyEi"
			+ "Iegn9QJAErpPxZldutBCuBxa3TDToIYZ9FPsI+dOxauEF8tx/l/v83gDDXCOSOUS"
			+ "PLVly/i6EFvXrkPkRWMI7F2m0XRX4QJAXjCJAuz+H0UqF+yr/+G6fiB67rxzRsCo"
			+ "ifcib2Hstt+cG0xevczXOx0bgE+7hu/9lr/w02/bq6YTSkYkT9BWPQJBAKjSpWJt"
			+ "CkAEhnTeTx16Xoh3qzxiwOClfx7GH3gGu7sdDOq5J4+emWmc7YH9ZcR/yzjOofng"
			+ "vwDa3njJSlRKX0Y=";
	
//	public static final String BASE64_STRING_PUBLIC_KEY = "MIGHAoGBAOKdQPT38V9fpaS+oRs3cDX5p40gFy60CK/GKNpVXbjS7JEmKDe4Aoyy"
//			+ "hSjOUA8MUkFOEnTLsNJ/KazHlMAzBYjzVg5QYtjv0wP4KCRwqb7Y2X3CgQNyNBhO"
//			+ "aCPQxCfmKeGbr9aq1ZPMXxhN8zWcfZn2mUdhD3ePyBIL3SN7RX8RAgED";	
//	public static final String BASE64_STRING_PRIVATE_KEY = "MIICXQIBAAKBgQDinUD09/FfX6WkvqEbN3A1+aeNIBcutAivxijaVV240uyRJig3"
//			+ "uAKMsoUozlAPDFJBThJ0y7DSfymsx5TAMwWI81YOUGLY79MD+CgkcKm+2Nl9woED"
//			+ "cjQYTmgj0MQn5inhm6/WqtWTzF8YTfM1nH2Z9plHYQ93j8gSC90je0V/EQIBAwKB"
//			+ "gQCXE4CjT/Y/lRkYfxYSJPV5URpeFWTJzVsf2XCRjj57N0hgxBrP0AGzIa4bNDVf"
//			+ "XYwriWGjMnXhqhvIhQ3Vd1kEtkBg9P9t+aKLp09+MH9vrW6xEmV/D+WdkdkNhf99"
//			+ "RolDqNL7UfkPxYFCxyEcUXvJiTl7vvKXlQcFyHruepieqwJBAPiV3dJVL5aBIED9"
//			+ "BWj4aj/iohL3PcPIa7wDNaA9WYmJEXw1P9gTiQIxPh06OTsUQmnvevPS8vfKD5ox"
//			+ "w/s+8SECQQDpX58OjoRi3hI8M+G+8i0U0NIT8YcWk0A3n1nnh5Jy0uqiPh3XzHMh"
//			+ "6/YGB1HnL6k+05LO0KE4c3nE8vnIIZ/xAkEApbk+jDjKZFYVgKiuRfrxf+xsDKTT"
//			+ "19ryfVd5FX47sQYLqCN/5WJbVst+vibQ0g2Bm/T8ooyh+oa1EXaCp39LawJBAJuV"
//			+ "FLRfAuyUDCgiln9MHg3gjA1Lr2RiKs+/kUUFDEyMnGwpaTqITMFH+VlaNpofxini"
//			+ "Yd81wNBM+9ih+9rBFUsCQQDqVtlzI18n8W4VVTaCMUDNLxHpF7SsJnzCDtfx7/yj"
//			+ "cm2lHeCJOlTC4xPaNOWmWf+ic3BgiBYhSp6ajjMk62se";
//	public static final String BASE64_STRING_PRIVATE_KEY_PKCS8 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOKdQPT38V9fpaS+"
//			+ "oRs3cDX5p40gFy60CK/GKNpVXbjS7JEmKDe4AoyyhSjOUA8MUkFOEnTLsNJ/KazH"
//			+ "lMAzBYjzVg5QYtjv0wP4KCRwqb7Y2X3CgQNyNBhOaCPQxCfmKeGbr9aq1ZPMXxhN"
//			+ "8zWcfZn2mUdhD3ePyBIL3SN7RX8RAgEDAoGBAJcTgKNP9j+VGRh/FhIk9XlRGl4V"
//			+ "ZMnNWx/ZcJGOPns3SGDEGs/QAbMhrhs0NV9djCuJYaMydeGqG8iFDdV3WQS2QGD0"
//			+ "/235oounT34wf2+tbrESZX8P5Z2R2Q2F/31GiUOo0vtR+Q/FgULHIRxRe8mJOXu+"
//			+ "8peVBwXIeu56mJ6rAkEA+JXd0lUvloEgQP0FaPhqP+KiEvc9w8hrvAM1oD1ZiYkR"
//			+ "fDU/2BOJAjE+HTo5OxRCae9689Ly98oPmjHD+z7xIQJBAOlfnw6OhGLeEjwz4b7y"
//			+ "LRTQ0hPxhxaTQDefWeeHknLS6qI+HdfMcyHr9gYHUecvqT7Tks7QoThzecTy+cgh"
//			+ "n/ECQQCluT6MOMpkVhWAqK5F+vF/7GwMpNPX2vJ9V3kVfjuxBguoI3/lYltWy36+"
//			+ "JtDSDYGb9PyijKH6hrURdoKnf0trAkEAm5UUtF8C7JQMKCKWf0weDeCMDUuvZGIq"
//			+ "z7+RRQUMTIycbClpOohMwUf5WVo2mh/GKeJh3zXA0Ez72KH72sEVSwJBAOpW2XMj"
//			+ "XyfxbhVVNoIxQM0vEekXtKwmfMIO1/Hv/KNybaUd4Ik6VMLjE9o05aZZ/6JzcGCI"
//			+ "FiFKnpqOMyTrax4=";
}
