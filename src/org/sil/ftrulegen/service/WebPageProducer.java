/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.util.ResourceBundle;

import org.sil.ftrulegen.model.*;

public class WebPageProducer {
	private static final WebPageProducer instance = new WebPageProducer();

	private RuleIdentifierAndParentSetter ruleIdSetter;

	private FLExTransRule rule;

	public static WebPageProducer getInstance() {
		return instance;
	}

	public final String produceWebPage(FLExTransRule rule, ResourceBundle bundle) {
		this.rule = rule;
		ruleIdSetter = RuleIdentifierAndParentSetter.getInstance();
		ruleIdSetter.setIdentifiersAndParents(rule);
		StringBuilder sb = new StringBuilder();
		sb.append(htmlBeginning());
		sb.append(htmlBody(bundle));
		sb.append(htmlEnding());
		return sb.toString();
	}

	private String htmlBeginning() {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		sb.append("<head><title>");
		sb.append(rule.getName());
		sb.append("</title>\n");
		sb.append("<meta charset=\"utf-8\"/>\n");
		sb.append("<link rel=\"stylesheet\" href=\"node_modules/treeflex/dist/css/treeflex.css\"/>\n");
		sb.append("<link rel=\"stylesheet\" href=\"rulegen.css\"/>\n");
		sb.append("<script>\n");
		sb.append(javaScriptContents());
		sb.append("</script>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		return sb.toString();
	}

	private String javaScriptContents() {
		StringBuilder sb = new StringBuilder();
		sb.append("function toApp(msg,event) {\n");
		sb.append("ftRuleGenApp.setXCoord(event.screenX);\n");
		sb.append("ftRuleGenApp.setYCoord(event.screenY);\n");
		sb.append("ftRuleGenApp.setItemClickedOn(msg);\n");
		sb.append("return false;\n");
		sb.append("}\n");
		return sb.toString();
	}

	private String htmlBody(ResourceBundle bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>\n");
		sb.append("<tr>\n");
		sb.append(phraseHTML(rule.getSource().getPhrase(), bundle));
		sb.append("<td>\n");
		sb.append("<span class=\"arrow\"/>\n");
		sb.append("</td>\n");
		sb.append(phraseHTML(rule.getTarget().getPhrase(), bundle));
		sb.append("</tr>\n");
		sb.append("</table>\n");
		return sb.toString();
	}

	private String phraseHTML(Phrase phrase, ResourceBundle bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append("<td valign=\"top\">\n");
		sb.append("<span class=\"tf-tree tf-gap-sm\">\n");
		sb.append("<ul>\n");
		sb.append(phrase.produceHtml(bundle));
		sb.append("</ul>\n");
		sb.append("</span>\n");
		sb.append("</td>\n");
		return sb.toString();
	}

	private String htmlEnding() {
		StringBuilder sb = new StringBuilder();
		sb.append("</body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}
}