package bean;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.ibm.commons.util.StringUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("encoderBean")
public class EncoderBean {
	public String urlEncode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
	
	public String url(String... parts) {
		return Arrays.stream(parts)
			.filter(StringUtil::isNotEmpty)
			.filter(p -> !"/".equals(p))
			.collect(Collectors.joining("/"));
	}
}
