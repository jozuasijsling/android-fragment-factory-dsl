package com.jsijsling.androidx.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Test

class InitializerFragmentFactoryTest {

    private val delegate: () -> Unit = { }

    @Test
    fun `emptyFragmentFactory can create parameterless fragments`() {
        val factory = fragmentFactory { }
        val fragment: Fragment = factory.instantiate<ParameterlessFragment>()
        assertNotNull(fragment)
    }

    @Test
    fun `emptyFragmentFactory cannot create parameterised fragments`() {
        val factory = fragmentFactory { }
        assertThrows(Fragment.InstantiationException::class.java) {
            factory.instantiate<ParameterisedFragmentA>()
        }
    }

    @Test
    fun `singleEntryFragmentFactory can create parameterised Fragment by its initializer`() {
        val factory = fragmentFactory {
            initializer {
                ParameterisedFragmentC(delegate)
            }
        }
        val fragment: Fragment = factory.instantiate<ParameterisedFragmentC>()
        assertThat(fragment, IsInstanceOf(ParameterisedFragmentC::class.java))
        assertEquals(delegate, (fragment as ParameterisedFragmentC).delegate)
    }

    @Test
    fun `singleEntryFragmentFactory can create Fragment with private constructor by its initializer`() {
        val factory = fragmentFactory {
            initializer {
                PrivateParameterlessFragment()
            }
        }
        val fragment: Fragment = factory.instantiate<PrivateParameterlessFragment>()
        assertNotNull(fragment)
    }

    @Test
    fun `multipleEntryFragmentFactory can create each fragment type for which an initializer was added`() {
        val factory = fragmentFactory {
            initializer {
                ParameterisedFragmentA(index = 42)
            }
            initializer {
                ParameterisedFragmentB(index = 1337)
            }
            initializer {
                ParameterisedFragmentC(delegate)
            }
        }

        val parameterisedFragmentA: Fragment = factory.instantiate<ParameterisedFragmentA>()
        val parameterisedFragmentB: Fragment = factory.instantiate<ParameterisedFragmentB>()
        val parameterisedFragmentC: Fragment = factory.instantiate<ParameterisedFragmentC>()

        assertThat(parameterisedFragmentA, IsInstanceOf(ParameterisedFragmentA::class.java))
        assertThat(parameterisedFragmentB, IsInstanceOf(ParameterisedFragmentB::class.java))
        assertThat(parameterisedFragmentC, IsInstanceOf(ParameterisedFragmentC::class.java))
        assertEquals(42, (parameterisedFragmentA as ParameterisedFragmentA).index)
        assertEquals(1337, (parameterisedFragmentB as ParameterisedFragmentB).index)
        assertSame(delegate, (parameterisedFragmentC as ParameterisedFragmentC).delegate)
    }

    @Test
    fun `initializerFragmentFactory can create other parameterless fragments`() {
        val factory = fragmentFactory {
            initializer {
                ParameterisedFragmentC(delegate)
            }
        }
        val fragment: Fragment = factory.instantiate<ParameterlessFragment>()
        assertNotNull(fragment)
    }

    @Test
    fun `initializerFragmentFactory cannot create other private parameterless fragments`() {
        val factory = fragmentFactory { }
        assertThrows(Fragment.InstantiationException::class.java) {
            factory.instantiate<PrivateParameterlessFragment>()
        }
    }

    @Test
    fun `initializer fragmentFactory cannot create other parameterised fragments`() {
        val factory = fragmentFactory {
            initializer {
                ParameterisedFragmentA(index = 42)
            }
        }
        assertThrows(Fragment.InstantiationException::class.java) {
            factory.instantiate<ParameterisedFragmentB>()
        }
    }

    private inline fun <reified T : Fragment> FragmentFactory.instantiate() =
        instantiate(T::class.java.classLoader!!, T::class.java.name)

    class ParameterlessFragment : Fragment()
    private class PrivateParameterlessFragment : Fragment()
    class ParameterisedFragmentA(val index: Int) : Fragment()
    class ParameterisedFragmentB(val index: Int) : Fragment()
    class ParameterisedFragmentC(val delegate: () -> Unit) : Fragment()
}
