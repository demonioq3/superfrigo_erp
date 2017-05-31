package cl.superfrigo.dao;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class Utils {


    public static String buildWhereClauses(Map<String, Object> filters) {
        StringBuilder whereClauses = new StringBuilder();

        if (filters != null) {
            for (String field : filters.keySet())
                appendFieldClause(whereClauses, field);
        }

        return whereClauses.toString();
    }

    public static void appendFieldClause(StringBuilder whereClauses, String field) {
        whereClauses.append(whereClauses.length() <= 0 ? " where d." : " and d.");
        whereClauses.append(field);

        if(field.equals("solicitante")){
            whereClauses.append(" like :solicitante");
            return;
        }

        if ( isDateField(field) ) {
            whereClauses.append(" >= :");
            whereClauses.append(dateFieldFrom(field));
            whereClauses.append(" and d.");
            whereClauses.append(field);
            whereClauses.append(" <= :");
            whereClauses.append(dateFieldTo(field));
        } else {
            whereClauses.append(isLikeField(field) ? " like :" : " = :");
            whereClauses.append(formattedField(field));
        }
    }

    public static void setFilterParams(Map<String, Object> filters, Query query) {
        if ( filters != null ) {
            for ( String field : filters.keySet() ) {
                setParameter(query, field, filters.get(field));
            }
        }
    }

    public static void setParameter(Query query, String field, Object value) {
        if(field.equals("solicitante")){
            query.setParameter(formattedField(field), "%" +value + "%");
            return;
        }
        if ( isDateField(field) ) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new SimpleDateFormat("d/M/yyyy").parse(simpleDateFormat.format(value));
                Calendar from = Calendar.getInstance();
                from.setTime(date);
                from.set(Calendar.HOUR_OF_DAY, 0);
                from.set(Calendar.MINUTE, 0);
                from.set(Calendar.SECOND, 0);
                from.set(Calendar.MILLISECOND, 0);

                query.setParameter(field, from.getTime());
            } catch (Exception e) {
                throw new IllegalArgumentException("Field value doesn't have the correct format (d/M/yyyy). " + field + ": " + value);
            }
        } else {
            query.setParameter(formattedField(field), formattedValue(field, value));
        }
    }

    public static Object formattedValue(String fieldName, Object fieldValue) {
        if (fieldValue == null || "".equals(fieldValue))
            return null;

        if ( isLikeField(fieldName) )
            return "%" +fieldValue + "%";

        if ( fieldName.toLowerCase().endsWith("id") )
            return Long.parseLong(String.valueOf(fieldValue));

        return fieldValue;
    }

    public static String formattedField(String field) {
        return field.replaceAll("\\.", "_");
    }

    public static String dateFieldFrom(String field) {
        return formattedField(field) + "_from";
    }

    public static String dateFieldTo(String field) {
        return formattedField(field) + "_to";
    }

    public static Boolean isLikeField(String field) {
        if(field.contains("codigoProducto")){
            return true;
        } else if(field.contains("descripcion")){
            return true;
        } else if(field.contains("descripcion2")){
            return true;
        }
        return field.contains(".id");
    }

    public static Boolean isDateField(String field) {
        if(field.equals("fechaInicio") || field.equals("fechaFin")){
            return true;
        }
        return field.toLowerCase().indexOf("date") >= 0;
    }
}
