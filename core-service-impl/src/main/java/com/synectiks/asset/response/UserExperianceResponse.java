package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExperianceResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Float score;
  
}
