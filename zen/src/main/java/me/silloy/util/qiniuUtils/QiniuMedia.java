package me.silloy.util.qiniuUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2017/6/24
 * Time: 18:06
 * CopyRight: Zhouji
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QiniuMedia {
    private String key;
    private String alias;
    private String downloadImg;
    private String hash;
}
