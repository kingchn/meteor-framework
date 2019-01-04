package cn.meteor.module.util.mapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;

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
	
	private static DozerBeanMapper dozerBeanMapper;

	static {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapper = mapperFactory.getMapperFacade();
		
		dozerBeanMapper = new DozerBeanMapper();
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