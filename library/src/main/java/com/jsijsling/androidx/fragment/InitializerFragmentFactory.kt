package com.jsijsling.androidx.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlin.reflect.KClass

/**
 * Creates an [InitializerFragmentFactory] with the initializers provided in the builder.
 */
public inline fun fragmentFactory(
    builder: InitializerFragmentFactoryBuilder.() -> Unit
): FragmentFactory = InitializerFragmentFactoryBuilder().apply(builder).build()

/**
 * DSL for constructing a new [InitializerFragmentFactory]
 */
@FragmentFactoryDsl
public class InitializerFragmentFactoryBuilder {
    private val initializers = mutableListOf<FragmentInitializer<*>>()

    /**
     * Add the initializer for the given Fragment class.
     *
     * @param clazz the class the initializer is associated with.
     * @param initializer lambda used to create an instance of the Fragment class
     */
    public fun <T : Fragment> addInitializer(clazz: KClass<T>, initializer: () -> T) {
        initializers.add(FragmentInitializer(clazz.java, initializer))
    }

    /**
     * Build the InitializerFragmentFactory.
     */
    public fun build(): FragmentFactory =
        InitializerFragmentFactory(*initializers.toTypedArray())
}

/**
 * Add an initializer to the [InitializerFragmentFactoryBuilder]
 */
public inline fun <reified F : Fragment> InitializerFragmentFactoryBuilder.initializer(
    noinline initializer: () -> F
) {
    addInitializer(F::class, initializer)
}

@DslMarker
private annotation class FragmentFactoryDsl

/**
 * Holds a [Fragment] class and initializer for that class
 */
private class FragmentInitializer<T : Fragment>(
    val clazz: Class<T>,
    val initializer: () -> T,
)

/**
 * A [FragmentFactory] that allows you to add lambda initializers for handling
 * particular Fragment classes, while using the default behavior for any
 * other classes.
 *
 * ```
 * supportFragmentManager.fragmentFactory = fragmentFactory {
 *   initializer { MyDialogFragment(arg1) }
 *   initializer { MyRegularFragment(arg2) }
 *   initializer { MyBottomSheetFragment(arg3) }
 * }
 * ```
 */
private class InitializerFragmentFactory(
    private vararg val initializers: FragmentInitializer<*>
) : FragmentFactory() {

    /**
     * Creates a new instance of the given `Class`.
     *
     * @param classLoader The default classloader to use for instantiation
     * @param className The class name of the fragment to instantiate.
     * @return a newly created Fragment
     */
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentInitializer = initializers.firstOrNull { it.clazz.name == className }
            ?: return super.instantiate(classLoader, className)
        return fragmentInitializer.initializer.invoke()
    }
}
