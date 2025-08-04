# android-fragment-factory-dsl

The missing DSL for composing a FragmentFactory. Largely a copy of
[InitializerViewModelFactoryBuilder](https://developer.android.com/reference/kotlin/androidx/lifecycle/viewmodel/InitializerViewModelFactoryBuilder)
from `androidx.lifecycle:lifecycle-viewmodel`.

## Usage

Create fragment factories with a friendly DSL format:

```kotlin
fragmentManager.fragmentFactory = fragmentFactory {
    initializer {
        CustomFragment1(
            customConfiguration,
            onComplete = { result ->
                viewModel.handleMyCustomFragmentResult(result)
            },
        )
    }
    initializer { CustomFragment2(customConfiguration) }
}
```

It is equivalent to:

```kotlin
fragmentManager.fragmentFactory = object : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            CustomFragment1::class.java.name -> CustomFragment1(
                customConfiguration,
                onComplete = { result ->
                    viewModel.handleMyCustomFragmentResult(result)
                },
            )
            CustomFragment2::class.java.name -> CustomFragment2(customConfiguration)
            else -> super.instantiate(classLoader, className)
        }
    }
}
```
