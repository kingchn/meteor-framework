package cn.meteor.module.core.freemarker.tags;

import freemarker.template.SimpleHash;

/**
 * Shortcut for injecting the tags into Freemarker
 *
 * <p>Usage: cfg.setSharedVeriable("m", new MTags());</p>
 */
public class MTags extends SimpleHash {
	
	public static final String TOKEN_NAME_FILED_FILED = "meteor.spring.token.field";
	
    public MTags() {
        put("onceToken", new OnceTokenTag());
    }
}