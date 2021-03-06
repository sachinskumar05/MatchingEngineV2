/*
 * Written by Gil Tene and Martin Thompson, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.sk.matchingenginev2.ObjectLayout;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Intrinsic objects (declared with the {@link Intrinsic @Intrinsic} annotation) may have
 * their layout within the containing object instance optimized by JDK implementations, such that access
 * to their content is faster, and avoids certain de-referencing steps.
 * <p>
 * The {@link IntrinsicObjects} class provides static methods for constructing objects
 * that are {@link Intrinsic @Intrinsic} to their containing class. Calls to
 * {@link IntrinsicObjects#constructWithin
 * constructWithin(String fieldName, Object containingObject)} (and variants) are used to construct and
 * initialize intrinsic objects associated with specific instance fields that are declared as
 * {@link Intrinsic @Intrinsic} in their containing class.
 * <p>
 * Some example of declaring an intrinsic object include:
 * <blockquote><pre>
 * public class Line {
 *     private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
 *     //
 *     // Simple intrinsic object declaration and initialization:
 *     //
 *     {@literal @}Intrinsic
 *     private final Point endPoint1 = IntrinsicObjects.constructWithin(lookup, "endPoint1", this);
 *     {@literal @}Intrinsic
 *     private final Point endPoint2 = IntrinsicObjects.constructWithin(lookup, "endPoint2", this);
 *     ...
 * }
 *
 * public class Octagon {
 *     private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
 *     //
 *     // Intrinsic object declaration and initialization for a StructuredArray member:
 *     //
 *     {@literal @}Intrinsic(length = 8)
 *     private final StructuredArray&lt;Point&gt; points = IntrinsicObjects.constructWithin(lookup, "points", this);
 *     ...
 * }
 * </pre></blockquote>
 *
 */
public final class IntrinsicObjects {

    // Prevent construction:
    private IntrinsicObjects() {
    }

    /**
     * Construct an intrinsic object at the given field within the containing object, using a default constructor.
     * <p>
     * The field specified in {@code fieldName} must be annotated with {@link Intrinsic @Intrinsic},
     * and must be declared private and final.
     *
     * @param lookup The lookup object to use for accessing the field
     * @param fieldName The name of the field within the containing object
     * @param containingObject The object instance that will contain this intrinsic object
     * @param <T> The type of the intrinsic object being constructed
     * @return A reference to the the newly constructed intrinsic object
     */
    public static <T> T constructWithin(
            MethodHandles.Lookup lookup,
            final String fieldName,
            final Object containingObject) {
        IntrinsicObjectModel<T> model = lookupModelFor(lookup, fieldName, containingObject);
        return model.constructWithin(containingObject);
    }

    /**
     * Construct an intrinsic object at the given field within the containing object, using the given
     * constructor and arguments.
     * <p>
     * The field specified in {@code fieldName} must be annotated with {@link Intrinsic @Intrinsic},
     * and must be declared private and final.
     *
     * @param lookup The lookup object to use for accessing the field
     * @param fieldName The name of the field within the containing object
     * @param containingObject The object instance that will contain this intrinsic object
     * @param objectConstructor The constructor to be used in constructing the intrinsic object instance
     * @param args the arguments to be used with the objectConstructor
     * @param <T> The type of the intrinsic object being constructed
     * @return A reference to the the newly constructed intrinsic object
     */
    public static <T> T constructWithin(
            MethodHandles.Lookup lookup,
            final String fieldName,
            final Object containingObject,
            final Constructor<T> objectConstructor,
            final Object... args) {
        IntrinsicObjectModel<T> model = lookupModelFor(lookup, fieldName, containingObject);
        return model.constructWithin(containingObject, objectConstructor, args);
    }

    /**
     * Construct an intrinsic object at the given field within the containing object, using the
     * constructor and arguments supplied in the given objectCtorAndArgs argument.
     * <p>
     * The field specified in {@code fieldName} must be annotated with {@link Intrinsic @Intrinsic},
     * and must be declared private and final.
     *
     * @param lookup The lookup object to use for accessing the field
     * @param fieldName The name of the field within the containing object
     * @param containingObject The object instance that will contain this intrinsic object
     * @param objectCtorAndArgs The constructor and arguments to be used in constructing the
     *                          intrinsic object instance
     * @param <T> The type of the intrinsic object being constructed
     * @return A reference to the the newly constructed intrinsic object
     */
    public static <T> T constructWithin(
            MethodHandles.Lookup lookup,
            final String fieldName,
            final Object containingObject,
            final CtorAndArgs<T> objectCtorAndArgs) {
        IntrinsicObjectModel<T> model = lookupModelFor(lookup, fieldName, containingObject);
        return model.constructWithin(containingObject, objectCtorAndArgs);
    }

    /**
     * Construct an intrinsic object at the given field within the containing object, using the
     * supplied {@link StructuredArrayBuilder}. This form of constructWithin() can only be used
     * to construct intrinsic objects that derive from {@link StructuredArray}.
     * <p>
     * The field specified in {@code fieldName} must be annotated with {@link Intrinsic @Intrinsic},
     * and must be declared private and final.
     *
     * @param lookup The lookup object to use for accessing the field
     * @param fieldName The name of the field within the containing object
     * @param containingObject The object instance that will contain this intrinsic object
     * @param arrayBuilder The {@link StructuredArrayBuilder} instance to be used in constructing the array
     * @param <T> The type of the intrinsic object being constructed
     * @return A reference to the the newly constructed intrinsic object
     */
    public static <T> T constructWithin(
            MethodHandles.Lookup lookup,
            final String fieldName,
            final Object containingObject,
            final StructuredArrayBuilder arrayBuilder) {
        IntrinsicObjectModel<T> model = lookupModelFor(lookup, fieldName, containingObject);
        return model.constructWithin(containingObject, arrayBuilder);
    }

    /**
     * Construct an intrinsic object at the given field within the containing object, using the
     * supplied {@link PrimitiveArrayBuilder}. This form of constructWithin() can only be used
     * to construct intrinsic objects that derive from {@link AbstractPrimitiveArray}.
     * <p>
     * The field specified in {@code fieldName} must be annotated with {@link Intrinsic @Intrinsic},
     * and must be declared private and final.
     *
     * @param lookup The lookup object to use for accessing the field
     * @param fieldName The name of the field within the containing object
     * @param containingObject The object instance that will contain this intrinsic object
     * @param arrayBuilder The {@link PrimitiveArrayBuilder} instance to be used in constructing the array
     * @param <T> The type of the intrinsic object being constructed
     * @return A reference to the the newly constructed intrinsic object
     */
    public static <T> T constructWithin(
            MethodHandles.Lookup lookup,
            final String fieldName,
            final Object containingObject,
            final PrimitiveArrayBuilder arrayBuilder) {
        IntrinsicObjectModel<T> model = lookupModelFor(lookup, fieldName, containingObject);
        return model.constructWithin(containingObject, arrayBuilder);
    }

    private static final
    ConcurrentHashMap<Class, HashMap<String, IntrinsicObjectModel>> modelsByClass =
            new ConcurrentHashMap<Class, HashMap<String, IntrinsicObjectModel>>();

    private static
    HashMap<String, IntrinsicObjectModel> lookupModelsByFieldNameForClass(
            MethodHandles.Lookup lookup,
            Object containingObject) {
        Class c = containingObject.getClass();
        HashMap<String, IntrinsicObjectModel> modelsByFieldName =
                modelsByClass.get(c);
        if (modelsByFieldName == null) {
            modelsByFieldName = new HashMap<String, IntrinsicObjectModel>();
            // Populate modelsByString hash map with all @Intrinsic fields found in the class:
            populateWithClassIntrinsicModels(lookup, containingObject, modelsByFieldName);
            modelsByClass.put(c, modelsByFieldName);
        }
        return modelsByFieldName;
    }

    private static <T> void populateWithClassIntrinsicModels(
            MethodHandles.Lookup lookup,
            Object containingObject,
            HashMap<String, IntrinsicObjectModel> modelsByFieldName) {
        Class c = containingObject.getClass();
        for (Field field : c.getDeclaredFields()) {
            if (field.getAnnotation(Intrinsic.class) != null) {
                IntrinsicObjectModel<T> objectModel = createModel(lookup, field);
                modelsByFieldName.put(field.getName(), objectModel);
            }
        }
    }

    private static <T> IntrinsicObjectModel<T> lookupModelFor(
            MethodHandles.Lookup lookup,
            String fieldName,
            Object containingObject) {
        HashMap<String, IntrinsicObjectModel> modelsByFieldName =
                lookupModelsByFieldNameForClass(lookup, containingObject);
        @SuppressWarnings("unchecked")
        IntrinsicObjectModel<T> objectModel =
                (IntrinsicObjectModel<T>) modelsByFieldName.get(fieldName);
        if (objectModel == null) {
            throw new IllegalArgumentException(
                    "No @Intrinsic field named \"" + fieldName + "\" found in " + containingObject.getClass());
        }
        return objectModel;
    }

    private static <T> IntrinsicObjectModel<T> createModel(MethodHandles.Lookup lookup, Field field) {
        @SuppressWarnings("unchecked")
        Class<T> objectClass = (Class<T>) field.getType();
        Class containingClass = field.getDeclaringClass();

        // Verify that the field has an @Intrinsic annotation:
        Intrinsic intrinsicAnnotation = field.getAnnotation(Intrinsic.class);
        if (intrinsicAnnotation == null) {
            throw new IllegalArgumentException("Field \"" + field.getName() + "\" in class " +
                    containingClass.getSimpleName() + " does not have an @Intrinsic annotation");
        }

        // Verify the the field is of a type derived from Object:
        if (!Object.class.isAssignableFrom(objectClass)) {
            throw new IllegalArgumentException("@Intrinsic annotations cannot be applied to primitive types");
        }

        PrimitiveArrayModel primitiveArrayModel = getPrimitiveArrayModel(field, intrinsicAnnotation);

        StructuredArrayModel structuredArrayModel = getStructuredArrayModel(field, intrinsicAnnotation);

        sanityCheckAnnotation(intrinsicAnnotation, objectClass);

        IntrinsicObjectModel<T> objectModel =
                new IntrinsicObjectModel<T>(
                        lookup,
                        field,
                        primitiveArrayModel,
                        structuredArrayModel
                );
        return objectModel;
    }

    private static long getLengthFromAnnotation(Intrinsic intrinsicAnnotation, Class objectClass) {
        long length = intrinsicAnnotation.length();
        if (length == Intrinsic.NO_LENGTH) {
            throw new IllegalArgumentException("length not specified. @Intrinsic annotations of " +
                    objectClass.getSimpleName() + " must specify length (via length = ...)");
        }
        if (length < 0) {
            throw new IllegalArgumentException("specified length must be positive.");
        }
        return length;
    }

    private static <T extends AbstractPrimitiveArray> PrimitiveArrayModel<T> getPrimitiveArrayModel(
            Field field, Intrinsic intrinsicAnnotation) {
        if (!(AbstractPrimitiveArray.class.isAssignableFrom(field.getType()))) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Class<T> objectClass = (Class<T>) field.getType();
        long length = getLengthFromAnnotation(intrinsicAnnotation, objectClass);
        return new PrimitiveArrayModel<T>(objectClass, length);
    }

    private static <T, S extends StructuredArray<T>> StructuredArrayModel<S, T>
    getStructuredArrayModel(Field field, Intrinsic intrinsicAnnotation) {
        if (!StructuredArray.class.isAssignableFrom(field.getType())) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<S> objectClass = (Class<S>) field.getType();

        long length = getLengthFromAnnotation(intrinsicAnnotation, objectClass);

        Class<T> elementClass = deriveElementClass(field, intrinsicAnnotation);

        return new StructuredArrayModel<S, T>(objectClass, elementClass, length){};
    }

    private static <T> Class<T> deriveElementClass(Field field, Intrinsic intrinsicAnnotation) {

        @SuppressWarnings("unchecked")
        Class<T> elementClass = intrinsicAnnotation.elementClass();
        Class objectClass = field.getType();

        if (!Object.class.isAssignableFrom(elementClass)) {
            throw new IllegalArgumentException("specified elementClass cannot be a primitive type");
        }

        // Try to infer elementClass from field:

        Type structuredArrayGenericType;

        if (objectClass.equals(StructuredArray.class)) {
            // objectClass is StructuredArray, see if we can infer the generic type:
            structuredArrayGenericType = field.getGenericType();
        } else {
            // objectClass is a subclass of StructuredArray, see if it establishes a concrete type:
            Class directSubClass = objectClass;
            while (!directSubClass.getSuperclass().equals(StructuredArray.class)) {
                directSubClass = directSubClass.getSuperclass();
            }
            structuredArrayGenericType = directSubClass.getGenericSuperclass();
        }

        try {
            ParameterizedType p = (ParameterizedType) structuredArrayGenericType;
            Type t = p.getActualTypeArguments()[0];
            if (t instanceof Class) {
                @SuppressWarnings("unchecked")
                Class<T> inferredElementClass = (Class<T>) t;
                if ((elementClass != Intrinsic.NO_DECLARED_CLASS.class) &&
                        (elementClass != inferredElementClass)) {
                    throw new IllegalArgumentException("Specified @Intrinsic elementClass (" + elementClass.getName() +
                            ") does not match inferred element class (" + inferredElementClass.getName() +
                            ") for field \"" + field.getName() + "\" (of type " + field.getGenericType() +
                            ") in " + field.getDeclaringClass());
                }
                elementClass = inferredElementClass;
            }
        } catch (ClassCastException ex) {
            // structuredArrayGenericType was not a ParameterizedType. Nothing to see here. Move along.
        }

        if (elementClass == Intrinsic.NO_DECLARED_CLASS.class) {
            throw new IllegalArgumentException(
                    "elementClass not specified. @Intrinsic annotations of " +
                            objectClass.getSimpleName() +
                            " must specify elementClass (via elementClass = ...)");
        }

        return elementClass;

    }

    private static <T> void sanityCheckAnnotation(Intrinsic intrinsicAnnotation, Class<T> objectClass) {

        boolean isPrimitiveArray = AbstractPrimitiveArray.class.isAssignableFrom(objectClass);
        boolean isStructuredArray = StructuredArray.class.isAssignableFrom(objectClass);

        if (!isPrimitiveArray && !isStructuredArray) {
            if (intrinsicAnnotation.length() != Intrinsic.NO_LENGTH) {
                throw new IllegalArgumentException(
                        "@Intrinsic annotations can only specify length for array types");
            }
        }

        if (!isStructuredArray) {
            if (intrinsicAnnotation.elementClass() != Intrinsic.NO_DECLARED_CLASS.class) {
                throw new IllegalArgumentException(
                        "@Intrinsic annotations can only specify elementClass for StructuredArray types");
            }
        }
    }
}
