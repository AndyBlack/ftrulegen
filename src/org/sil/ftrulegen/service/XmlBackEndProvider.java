/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.*;

import org.sil.ftrulegen.model.*;
import org.sil.utility.HandleExceptionMessage;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class XmlBackEndProvider extends BackEndProvider {
	private FLExTransRuleGenerator ruleGenerator;
	ResourceBundle bundle;

	public XmlBackEndProvider(FLExTransRuleGenerator ruleGenerator, ResourceBundle bundle) {
		this.ruleGenerator = ruleGenerator;
		this.bundle = bundle;
		setResourceStrings(bundle.getLocale());
	}

	public FLExTransRuleGenerator getRuleGenerator() {
		return ruleGenerator;
	}

	public void setRuleGenerator(FLExTransRuleGenerator value) {
		ruleGenerator = value;
	}

	public final void loadDataFromFile(String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				JAXBContext context = JAXBContext.newInstance(FLExTransRuleGenerator.class);
				Unmarshaller um = context.createUnmarshaller();
				// Reading XML from the file and unmarshalling.
				ruleGenerator = (FLExTransRuleGenerator) um.unmarshal(file);
				// Not sure we need this, but otherwise the target phrase type is set to source
				for (FLExTransRule rule : getRuleGenerator().getFLExTransRules()) {
					rule.getTarget().getPhrase().setType(PhraseType.target);
					setCategoryConstituentInWords(rule.getSource().getPhrase().getWords());
					setCategoryConstituentInWords(rule.getTarget().getPhrase().getWords());
				}
			} else {
				Path filePath = Paths.get(fileName);
				Files.createFile(filePath);
//				// ensure there is at least one (empty) rule
				FLExTransRule newRule = new FLExTransRule();
				ruleGenerator.getFLExTransRules().add(newRule);
				newRule.getSource().getPhrase().insertNewWordAt(0);
				newRule.getTarget().getPhrase().insertNewWordAt(0);
				newRule.setBundle(bundle);
				saveDataToFile(fileName);
				loadDataFromFile(fileName);
			}
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			HandleExceptionMessage.show(sFileError, sFileErrorLoadHeader, sFileErrorLoadContent + fileName, true);
		}
	}

	private static void setCategoryConstituentInWords(List<Word> words) {
		for (Word word : words) {
			word.getCategoryConstituent().setName(word.getCategory());
		}
	}

	public final void saveDataToFile(String fileName) {
		try {
			File file = new File(fileName);
			JAXBContext context = JAXBContext.newInstance(FLExTransRuleGenerator.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// Marshalling and saving XML to the file.
			m.marshal(ruleGenerator, file);

			// Maybe there's a better way, but this hack inserts the DOCTYPE in the right
			// spot.
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			sb.append("<!DOCTYPE FLExTransRuleGenerator PUBLIC \" -//XMLmind//DTD FLExTransRuleGenerator//EN\"\n");
			sb.append("\"FLExTransRuleGenerator.dtd\">\n");
			String result = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			sb.append("<FLExTransRuleGenerator>\n");
			sb.append(result.substring(81));
			Files.write(file.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			HandleExceptionMessage.show(sFileError, sFileErrorSaveHeader, sFileErrorSaveContent + fileName, true);
		}
	}
}