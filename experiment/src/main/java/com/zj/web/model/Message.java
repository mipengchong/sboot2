package com.zj.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/23
 * Time: 9:45
 * Description: MAIN
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String body;
}
