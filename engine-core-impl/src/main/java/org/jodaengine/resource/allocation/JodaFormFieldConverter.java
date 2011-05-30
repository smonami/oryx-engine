package org.jodaengine.resource.allocation;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.allocation.JodaFormAttributes;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.OutputDocument;



/**
 * The Class JodaFormFieldConverterImpl.
 */
public class JodaFormFieldConverter {

    /**
     * Converts joda:*-Form-Attributes to JodaFormFields. If the supplied class cannot be resolved, {@link String} is
     * used per default.
     * 
     * @param formAttributes
     *            the form attributes
     * @return the joda form field
     */
    public JodaFormField convert(Attributes formAttributes) {

        String name = formAttributes.getValue("name");

        // remove the attributes from the form field, as we have read them and they are no valid html.
        Map<String, String> attributes = new HashMap<String, String>();

        attributes.put(JodaFormAttributes.READ_VARIABLE, formAttributes.getValue(JodaFormAttributes.READ_VARIABLE));
        attributes.put(JodaFormAttributes.WRITE_VARIABLE, formAttributes.getValue(JodaFormAttributes.WRITE_VARIABLE));
        attributes.put(JodaFormAttributes.READ_EXPRESSION, formAttributes.getValue(JodaFormAttributes.READ_EXPRESSION));
        attributes.put(JodaFormAttributes.WRITE_EXPRESSION,
            formAttributes.getValue(JodaFormAttributes.WRITE_EXPRESSION));

        String className = formAttributes.getValue(JodaFormAttributes.CLASS_NAME);

        Class<?> dataClass;
        if (className != null) {
            try {
                dataClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                dataClass = String.class;
            }
        } else {
            dataClass = String.class;
        }

        // TODO REVIEW Wieso eine jodaAttributes Map und keine konkrete Implementierung mit 
        //             Standardwerten, falls die Felder nicht gesetzt sind?
        JodaFormField formField = new JodaFormFieldImpl(name, attributes, dataClass);

        return formField;
    }

    /**
     * Removes all JodaAttributes from the OutputDocument.
     */
    public void cleanJodaAttributes(Attributes attributes, OutputDocument document) {

        Map<String, String> replacements = document.replace(attributes, false);
        replacements.remove(JodaFormAttributes.READ_VARIABLE);
        replacements.remove(JodaFormAttributes.WRITE_VARIABLE);
        replacements.remove(JodaFormAttributes.READ_EXPRESSION);
        replacements.remove(JodaFormAttributes.WRITE_EXPRESSION);
    }

}
