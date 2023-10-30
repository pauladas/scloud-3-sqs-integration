package br.com.paulojof.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CustomParameterNameDiscoverer implements ParameterNameDiscoverer {

	@Override
	public String[] getParameterNames(Method method) {
		return parameterNames(method);
	}

	@Override
	public String[] getParameterNames(Constructor<?> ctor) {
		return parameterNames(ctor);
	}

	private String[] parameterNames(Executable executable) {
		List<String> parameterNames = new ArrayList<>();
		Stream.of(executable.getParameters()).forEach(param -> {
			RequestParam requestParamAnnotation = param.getAnnotation(RequestParam.class);

			String name = Optional.ofNullable(requestParamAnnotation).stream()
					.map(RequestParam::name)
					.filter(StringUtils::isNotBlank)
					.findFirst()
					.orElse(param.getName());

			parameterNames.add(name);
		});
		return parameterNames.toArray(new String[0]);
	}

}
