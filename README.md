# How to reproduce the leak

1. Run the app
2. Click the "Open Bar screen"
3. Change the orientation of the device from portrait to landscape
4. Change the orientation back to portrait

At this point you should see StrictMode complaining about the leak inside the logcat:
```
2021-11-02 16:23:20.672 31230-31230/com.leinardi.template E/StrictMode: class com.leinardi.template.ui.MainActivity; instances=2; limit=1
    android.os.StrictMode$InstanceCountViolation: class com.leinardi.template.ui.MainActivity; instances=2; limit=1
        at android.os.StrictMode.setClassInstanceLimit(StrictMode.java:1)
```

Should also be visible the Leakcanary notification.

```
​
┬───
│ GC Root: System class
│
├─ leakcanary.internal.InternalLeakCanary class
│    Leaking: NO (MainActivity↓ is not leaking and a class is never leaking)
│    ↓ static InternalLeakCanary.resumedActivity
├─ com.leinardi.template.ui.MainActivity instance
│    Leaking: NO (Activity#mDestroyed is false)
│    mApplication instance of com.leinardi.template.Template
│    mBase instance of android.app.ContextImpl
│    ↓ MainActivity.navHostController
│                   ~~~~~~~~~~~~~~~~~
├─ androidx.navigation.NavHostController instance
│    Leaking: UNKNOWN
│    Retaining 5.5 kB in 140 objects
│    activity instance of com.leinardi.template.ui.MainActivity with mDestroyed = false
│    context instance of com.leinardi.template.ui.MainActivity with mDestroyed = false
│    lifecycleOwner instance of com.leinardi.template.ui.MainActivity with mDestroyed = false
│    ↓ NavController.viewModel
│                    ~~~~~~~~~
├─ androidx.navigation.NavControllerViewModel instance
│    Leaking: UNKNOWN
│    Retaining 128.4 kB in 2406 objects
│    ↓ NavControllerViewModel.viewModelStores
│                             ~~~~~~~~~~~~~~~
├─ java.util.LinkedHashMap instance
│    Leaking: UNKNOWN
│    Retaining 128.3 kB in 2404 objects
│    ↓ LinkedHashMap.header
│                    ~~~~~~
├─ java.util.LinkedHashMap$LinkedHashMapEntry instance
│    Leaking: UNKNOWN
│    Retaining 32 B in 1 objects
│    ↓ LinkedHashMap$LinkedHashMapEntry.after
│                                       ~~~~~
├─ java.util.LinkedHashMap$LinkedHashMapEntry instance
│    Leaking: UNKNOWN
│    Retaining 127.8 kB in 2389 objects
│    ↓ HashMap$HashMapEntry.value
│                           ~~~~~
├─ androidx.lifecycle.ViewModelStore instance
│    Leaking: UNKNOWN
│    Retaining 127.7 kB in 2388 objects
│    ↓ ViewModelStore.mMap
│                     ~~~~
├─ java.util.HashMap instance
│    Leaking: UNKNOWN
│    Retaining 127.7 kB in 2387 objects
│    ↓ HashMap.table
│              ~~~~~
├─ java.util.HashMap$HashMapEntry[] array
│    Leaking: UNKNOWN
│    Retaining 127.7 kB in 2385 objects
│    ↓ HashMap$HashMapEntry[].[1]
│                             ~~~
├─ java.util.HashMap$HashMapEntry instance
│    Leaking: UNKNOWN
│    Retaining 126.6 kB in 2348 objects
│    ↓ HashMap$HashMapEntry.value
│                           ~~~~~
├─ androidx.navigation.compose.BackStackEntryIdViewModel instance
│    Leaking: UNKNOWN
│    Retaining 126.5 kB in 2347 objects
│    ↓ BackStackEntryIdViewModel.saveableStateHolder
│                                ~~~~~~~~~~~~~~~~~~~
├─ androidx.compose.runtime.saveable.SaveableStateHolderImpl instance
│    Leaking: UNKNOWN
│    Retaining 126.4 kB in 2343 objects
│    ↓ SaveableStateHolderImpl.parentSaveableStateRegistry
│                              ~~~~~~~~~~~~~~~~~~~~~~~~~~~
├─ androidx.compose.ui.platform.DisposableSaveableStateRegistry instance
│    Leaking: UNKNOWN
│    Retaining 125.9 kB in 2321 objects
│    ↓ DisposableSaveableStateRegistry.onDispose
│                                      ~~~~~~~~~
├─ androidx.compose.ui.platform.DisposableSaveableStateRegistry_androidKt$DisposableSaveableStateRegistry$1 instance
│    Leaking: UNKNOWN
│    Retaining 125.6 kB in 2312 objects
│    Anonymous subclass of kotlin.jvm.internal.Lambda
│    ↓ DisposableSaveableStateRegistry_androidKt$DisposableSaveableStateRegistry$1.$androidxRegistry
│                                                                                  ~~~~~~~~~~~~~~~~~
├─ androidx.savedstate.SavedStateRegistry instance
│    Leaking: UNKNOWN
│    Retaining 125.6 kB in 2310 objects
│    ↓ SavedStateRegistry.mComponents
│                         ~~~~~~~~~~~
├─ androidx.arch.core.internal.SafeIterableMap instance
│    Leaking: UNKNOWN
│    Retaining 125.5 kB in 2309 objects
│    ↓ SafeIterableMap.mEnd
│                      ~~~~
├─ androidx.arch.core.internal.SafeIterableMap$Entry instance
│    Leaking: UNKNOWN
│    Retaining 125.3 kB in 2302 objects
│    ↓ SafeIterableMap$Entry.mValue
│                            ~~~~~~
├─ androidx.activity.ComponentActivity$$ExternalSyntheticLambda1 instance
│    Leaking: UNKNOWN
│    Retaining 125.3 kB in 2301 objects
│    f$0 instance of com.leinardi.template.ui.MainActivity with mDestroyed = true
│    ↓ ComponentActivity$$ExternalSyntheticLambda1.f$0
│                                                  ~~~
╰→ com.leinardi.template.ui.MainActivity instance
​     Leaking: YES (ObjectWatcher was watching this because com.leinardi.template.ui.MainActivity received
​     Activity#onDestroy() callback and Activity#mDestroyed is true)
​     Retaining 125.3 kB in 2300 objects
​     key = 2c6ea34a-19c0-4d26-a5ec-88625d79531f
​     watchDurationMillis = 42581
​     retainedDurationMillis = 37580
​     mApplication instance of com.leinardi.template.Template
​     mBase instance of android.app.ContextImpl
METADATA
Build.VERSION.SDK_INT: 24
Build.MANUFACTURER: Google
LeakCanary version: 2.7
App process name: com.leinardi.template
Stats: LruCache[maxSize=3000,hits=1619,misses=30537,hitRate=5%]
RandomAccess[bytes=1518368,reads=30537,travel=10623307269,range=18917491,size=20769812]
Heap dump reason: user request
Analysis duration: 1249 ms
```

### Things I have noticed:

- the leak happens only after the 2nd change of orientation (so, if you start with the phone in portrait, when you go back from landscape to portrait)
- the leak happens also on the Debug screen, if I navigate to it using the nav component
- the leak does not happen if the Bar screen is accessed via deep link (`adb shell am start -d "template://bar" -a android.intent.action.VIEW`)
- the leak does not happen if use the navigate up button of the top app bar to go back from Bar screen to Foo screen (Foo screen never leaks)
- reproducible on API level 24, 28 and 30 (I did not test on other levels)

### Things I've already tried:
- Remove the instance of the nav host from the activity (`lateinit var navHostController: NavHostController`)
- Use `rememberCoroutineScope()` instead of `LaunchedEffect()` for the `templateNavigator.destinations.collect {}` (MainActivity.kt:71)
