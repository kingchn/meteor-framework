package cn.meteor.module.core.openApi.logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpServletResponseCopier extends HttpServletResponseWrapper {

    private ServletOutputStream servletOutputStream;
    private PrintWriter writer;
    private ServletOutputStreamCopier servletOutputStreamCopier;

    public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (servletOutputStream == null) {
            servletOutputStream = getResponse().getOutputStream();
            servletOutputStreamCopier = new ServletOutputStreamCopier(servletOutputStream);
        }

        return servletOutputStreamCopier;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (servletOutputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            servletOutputStreamCopier = new ServletOutputStreamCopier(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(servletOutputStreamCopier, getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (servletOutputStream != null) {
            servletOutputStreamCopier.flush();
        }
    }

    public byte[] getCopy() {
        if (servletOutputStreamCopier != null) {
            return servletOutputStreamCopier.getCopy();
        } else {
            return new byte[0];
        }
    }

}
