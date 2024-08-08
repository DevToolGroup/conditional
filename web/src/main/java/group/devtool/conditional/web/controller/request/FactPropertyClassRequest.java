package group.devtool.conditional.web.request;

import lombok.Data;

@Data
public class FactPropertyClassRequest {

	private Integer id;

	private String code;

	private String name;

	private String type;

	private String valueType;

	private String keyType;

}