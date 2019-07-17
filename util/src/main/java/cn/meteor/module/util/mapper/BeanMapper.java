package cn.meteor.module.util.mapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MappingException;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * 封装orika, 实现深度的BeanOfClasssA<->BeanOfClassB复制	pom文件引用orika
 * 
 * 封装dozer，实现map和对象相互转换 pom文件引用dozer
 * 
 * 不用Apache Common BeanUtils进行类复制，每次就行反射查询对象的属性列表, 非常缓慢.
 * 
 */
public class BeanMapper {

	private static MapperFacade mapper;
	
//	private static DozerBeanMapper dozerBeanMapper;
	private static Mapper dozerBeanMapper;

//	public static String PLACEHODER_DATEFORMAT = "${dateFormat}"; 
//	
//	public static String configurationXml =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<mappings xmlns=\"http://dozer.sourceforge.net\"\n" +
//                    "          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
//                    "          xsi:schemaLocation=\"http://dozer.sourceforge.net http://dozer.sourceforge.net/schema/beanmapping.xsd\">\n" +
//                    "  <configuration>\n" +
//                    "    <date-format>" + PLACEHODER_DATEFORMAT  + "</date-format>\n" +
//                    "  </configuration>\n" +
//                    "</mappings>";
//	
//	
//	public static String getConfirurationXmlString(String dateFormat) {
//		String configurationXmlString = configurationXml.replace(PLACEHODER_DATEFORMAT, dateFormat);
//		return configurationXmlString;
//	}


	static {			    
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapper = mapperFactory.getMapperFacade();		
		
//		旧版本(net.sf.dozer) 通过配置文件初始化
//		dozerBeanMapper = new DozerBeanMapper();
////		dozerBeanMapper.setMappingFiles(Arrays.asList("dozer/dozer-global-configuration.xml"));//yyyy-MM-dd'T'HH:mm:ss.SSS
//		
//		旧版本(net.sf.dozer) 通过xml字符串初始化
//		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
//		String configurationXmlString = getConfirurationXmlString(dateFormat);
//		dozerBeanMapper.addMapping(new ByteArrayInputStream(configurationXmlString.getBytes()));
		
		dozerBeanMapper = DozerBeanMapperBuilder.buildDefault();
		
	}
	

	/**
	 * 适用于map转对象
	 * @param source
	 * @param destinationClass
	 * @return
	 * @throws MappingException
	 */
	public static <D> D mapWithDozer(Object source, Class<D> destinationClass) throws MappingException {
		return dozerBeanMapper.map(source, destinationClass);
	}
	
	/**
	 * 适用于对象转map
	 * @param source
	 * @param destination
	 * @throws MappingException
	 */
	public static void mapWithDozer(Object source, Object destination) throws MappingException {
		dozerBeanMapper.map(source, destination);
	}
	
	public <S, D> MapperFacade getCustomMapperFacade(Class<S> sourceClass, Class<D> destinationClass, Map<String, String> customMap) {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		MapperFacade  mapper = mapperFactory.getMapperFacade();
		ClassMapBuilder<S, D> classMapBuilder = mapperFactory.classMap(sourceClass, destinationClass);
		for (String key : customMap.keySet()) {
			String target = customMap.get(key);
			if(StringUtils.isNotBlank(target)) {
				classMapBuilder.field(key, target);
			} else {
				classMapBuilder.exclude(key);
			}
		}
		classMapBuilder.byDefault().register();
		return mapper;
	}

	/**
	 * 简单的复制出新类型对象.
	 * 
	 * 通过source.getClass() 获得源Class
	 */
	public static <S, D> D map(S source, Class<D> destinationClass) {
		return mapper.map(source, destinationClass);
	}
	
	/**
	 * 简单的复制出新类型对象.(支持自定义map)
	 * 
	 * 通过source.getClass() 获得源Class
	 */
	public static <S, D> D customMap(S source, Class<S> sourceClass, Class<D> destinationClass, Map<String, String> customMap) {
		BeanMapper beanMapper = new BeanMapper();   
		MapperFacade mapper = beanMapper.getCustomMapperFacade(sourceClass, destinationClass, customMap);
		return mapper.map(source, destinationClass);
	}

	/**
	 * 极致性能的复制出新类型对象.
	 * 
	 * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
	 */
	public static <S, D> D map(S source, Type<S> sourceType, Type<D> destinationType) {
		return mapper.map(source, sourceType, destinationType);
	}

	/**
	 * 简单的复制出新对象列表到ArrayList
	 * 
	 * 不建议使用mapper.mapAsList(Iterable<S>,Class<D>)接口, sourceClass需要反射，实在有点慢
	 */
	public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
		return mapper.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
	}
	
	/**
	 * 简单的复制出新对象列表到ArrayList(支持自定义map)
	 * 
	 * 不建议使用mapper.mapAsList(Iterable<S>,Class<D>)接口, sourceClass需要反射，实在有点慢
	 */
	public static <S, D> List<D> customMapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass, Map<String, String> customMap) {
		BeanMapper beanMapper = new BeanMapper();   
		MapperFacade mapper = beanMapper.getCustomMapperFacade(sourceClass, destinationClass, customMap);
		return mapper.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
	}

	/**
	 * 极致性能的复制出新类型对象到ArrayList.
	 * 
	 * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
	 */
	public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
		return mapper.mapAsList(sourceList, sourceType, destinationType);
	}

	/**
	 * 简单复制出新对象列表到数组
	 * 
	 * 通过source.getComponentType() 获得源Class
	 */
	public static <S, D> D[] mapArray(final D[] destination, final S[] source, final Class<D> destinationClass) {
		return mapper.mapAsArray(destination, source, destinationClass);
	}

	/**
	 * 极致性能的复制出新类型对象到数组
	 * 
	 * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
	 */
	public static <S, D> D[] mapArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
		return mapper.mapAsArray(destination, source, sourceType, destinationType);
	}

	/**
	 * 预先获取orika转换所需要的Type，避免每次转换.
	 */
	public static <E> Type<E> getType(final Class<E> rawType) {
		return TypeFactory.valueOf(rawType);
	}

}