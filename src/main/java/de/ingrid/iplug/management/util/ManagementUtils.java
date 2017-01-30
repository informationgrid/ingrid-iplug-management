/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.iplug.management.util;

import de.ingrid.iplug.management.ManagementIPlug;
import de.ingrid.utils.query.FieldQuery;
import de.ingrid.utils.query.IngridQuery;

/**
 * Utils class for the management iplug.
 * 
 * @author joachim@wemove.com
 */
public class ManagementUtils {

    /**
     * Get a fields content as String.
     * 
     * @param query
     *            The query to check;
     * @param fieldName
     *            The field name to look for.
     * @return The result as a string. null if not found or the field is not of
     *         the type String.
     */
    public static String getField(IngridQuery query, String fieldName) {
        FieldQuery[] fields = query.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getFieldName().equalsIgnoreCase(fieldName)) {
                Object obj = fields[i].getFieldValue();
                if (obj instanceof String) {
                    return (String) obj;
                }
                break;
            }
        }
        return null;
    }

    /**
     * Checks if the array of datatypes contains a management datatype.
     * 
     * @param dataTypes
     *            The datatypes to check.
     * @return True if the management datatype exists, false, if not.
     */
    public static boolean containsManagementDataType(FieldQuery[] dataTypes) {
        int count = dataTypes.length;
        for (int i = 0; i < count; i++) {
            FieldQuery query = dataTypes[i];
            if (query.getFieldValue().equals(ManagementIPlug.DATATYPE_MANAGEMENT) && !query.isProhibited()) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the language of the query i set.
     * 
     * @param query
     *            The query.
     * @return The language of the query, null if the language was not set.
     */
    public static String getQueryLang(IngridQuery query) {
        String result = null;

        FieldQuery[] qFields = query.getFields();
        for (int i = 0; i < qFields.length; i++) {
            final String fieldName = qFields[i].getFieldName();
            if (fieldName.equals("lang")) {
                result = qFields[i].getFieldValue();
            }
        }

        return result;
    }

}
