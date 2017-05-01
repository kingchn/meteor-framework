package cn.meteor.module.core.openApi.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import jodd.io.StreamUtil;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 两个方法都注明方法只能被调用一次，由于RequestBody是流的形式读取，那么流读了一次就没有了，所以只能被调用一次。
	 * 既然是因为流只能读一次的原因，那么只要将流的内容保存下来，就可以实现反复读取了。
	 * byte数组允许被多次读取，而不会丢失内容。下面使用byte数组将流的内容保存下来。
	 */
	private final byte[] body;

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// body = StreamUtil.readBytes(request.getReader(),JoddDefault.encoding);
		// 因为http协议默认传输的编码就是iso-8859-1,如果使用utf-8转码乱码的话，可以尝试使用iso-8859-1
		body = StreamUtil.readBytes(request.getReader(), "iso-8859-1");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

}
