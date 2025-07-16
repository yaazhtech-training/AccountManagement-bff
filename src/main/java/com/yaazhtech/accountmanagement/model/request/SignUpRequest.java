package com.yaazhtech.accountmanagement.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
public class SignUpRequest implements Serializable {
    private static final long serialVersionUID = 12348L;

    @Schema(example = "Rajappan S")

    private String name;

    @Schema(example = "user3@mycompany.com")
//    @Pattern(
//            regexp = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
//                    + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$",
//            message = "Email not matched with pattern"
//    )
//    @NotBlank
    private String email;

//    @Pattern(
//            regexp = "(^([0]|\\+91)?\\d{10})",
//            message = "Mobile number must be length 10. ex: 0 or +91 6382289060"
//    )
//    @NotBlank
    private String mobile;

    @Schema(example = "password123")
    private String password;

    @Schema(example = "Other")
    private String gender;

    @Schema(example = "ponmugil")
    private String orgName;

    @Schema(example = "Team Manager") // could also be Individual Player, Contest Joiner
    private String category;

    @Schema(example = "Team 1")
    private String teamName;
    @Schema(example = "aadharNo")
    private String aadharNo;

    @Schema(example = "panNo")
    private String panNo;
    @Schema(example = "playerRole")
    private String playerRole;


//    private List<PlayerRequest> playerList;
}
