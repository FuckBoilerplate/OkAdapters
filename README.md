# OkAdapters
Wrappers for Android adapters to simply its api at a minimum.   

## Setup
Add OkAdapter dependency to project level build.gradle.

```gradle
dependencies {
    compile 'com.github.miguelbcr:OkAdapters:0.2.1'
}
```

Add jitpack repository to root level build.gradle.

```gradle
allprojects {
    repositories {
        //..
        maven { url "https://jitpack.io" }
    }
}

```

## Usage

OkAdapters provides several adapters to deal with specific Android `views`.

## RecyclerView
Create a class which extends from any Android `ViewGroup` and implements `OkRecyclerViewAdapter.Binder`. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
 public class YourModelViewGroup extends FrameLayout implements OkRecyclerViewAdapter.Binder<YourModel> {
 
     public YourModelViewGroup(Context context) {
         super(context);
 
         View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
         ButterKnife.bind(this, view);
     }
  
     @Bind(R.id.tv_value) TextView tv_value;

     @Override public void bind(YourModel model, int position) {
        tv_value.setText(model.getValue());
     }
     
 }
 
```
 
Now instantiate [OkRecyclerViewAdapter](https://github.com/miguelbcr/OkAdapters/blob/master/ok_adapters/src/main/java/miguelbcr/ui/recycler_view/OkRecyclerViewAdapter.java) using the previous `OkRecyclerViewAdapter.Binder` implementation class and use it as a normal `adapter`.

```java
 OkRecyclerViewAdapter<YourModel, YourModelViewGroup> adapter = new OkRecyclerViewAdapter<YourModel, YourModelViewGroup>() {
     @Override protected YourModelViewGroup onCreateItemView(ViewGroup parent, int viewType) {
         return new YourModelViewGroup(parent.getContext());
     }
 };
 
 recyclerView.setAdapter(adapter);
 
```
  
### RecyclerView pagination.

`OkRecyclerViewAdapter` supports a pagination. In order to use this feature, you need to call `setPager` from the adapter with the following arguments:


1. A valid reference to the layout which will be used as the loading row when requesting successive items 
2. The already loaded items (for handling config changes). 
3. An implementation of the interface `LoaderPager`, which exposes the last visible item and request an instance of the interface `Call` for retrieving the data in an async way. 

```java
adapter.setPager(R.layout.loading_pager, presenter.getUsersState(),
    new Pager.LoaderPager<YourModel>() {
      @Override public Pager.Call<YourModel> onNextPage(@Nullable YourModel lastItem) {
        return new Pager.Call<YourModel>() {
          @Override public void execute(Pager.Callback<YourModel> callback) {
            callback.supply(models);
          }
        };
      }
    });    
```

It is also possible call to `resetPager` in order to restart the pagination, supplying instance of the interface `Call`.
  
```java
adapter.resetPager(new Pager.Call<User>() {
    @Override public void execute(Pager.Callback<User> callback) {
      callback.supply(models);
    }
  });
```
  
## Spinner
Create a class which extends from any Android `ViewGroup` and implements `OkSpinnerAdapter.Binder` and `OkSpinnerAdapter.BinderDropDown` for the same `view` or for two different `views` which implement each interface separately. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
public class YourModelViewGroup extends FrameLayout implements OkSpinnerAdapter.Binder<YourModel>, OkSpinnerAdapter.BinderDropDown<YourModel> {
    @Bind(R.id.tv_value) TextView tv_value;

    public YourModelViewGroup(Context context) {
        super(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindDropDownView(YourModel model, int position) {
        tv_value.setText(model.getValue());
    }

    @Override
    public void bindView(YourModel model, int position) {
        tv_value.setText(model.getValue());
    }
}  
```
  
  Now instantiate [OkSpinnerAdapter](https://github.com/miguelbcr/OkAdapters/blob/master/ok_adapters/src/main/java/miguelbcr/ui/spinner/OkSpinnerAdapter.java) using the previous `OkSpinnerAdapter.Binder` and `OkSpinnerAdapter.BinderDropDown` implementation class and use it as a normal `adapter`.

```java 
    List<YourModel> items = getItems();
 
    OkSpinnerAdapter<YourModel, YourModelViewGroup, YourModelViewGroup> adapter = new OkSpinnerAdapter<YourModel, YourModelViewGroup, YourModelViewGroup>(context, items) {
        @Override
        public YourModelViewGroup inflateView() {
            return new YourModelViewGroup(context);
        }

        @Override
        public YourModelViewGroup inflateDropDownView() {
            return new YourModelViewGroup(context);
        }
    };
    
    spinner.setAdapter(adapter);
 
```
  

## BaseAdapter
Create a class which extends from any Android `ViewGroup` and implements `OkBaseAdapter.Binder`. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
 public class YourModelViewGroup extends FrameLayout implements OkBaseAdapter.Binder<YourModel> {
 
     public YourModelViewGroup(Context context) {
         super(context);
 
         View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
         ButterKnife.bind(this, view);
     }
  
     @Bind(R.id.tv_value) TextView tv_value;

     @Override public void bind(YourModel model, int position) {
        tv_value.setText(model.getValue());
     }
     
 }
 
```
 
Now instantiate [OkBaseAdapter](https://github.com/miguelbcr/OkAdapters/blob/master/ok_adapters/src/main/java/miguelbcr/ui/base_adapter/OkBaseAdapter.java) using the previous `OkBaseAdapter.Binder` implementation class and use it as a normal `adapter`.

```java
OkBaseAdapter<Item, ItemViewGroup> adapter = new OkBaseAdapter<YourModel, YourModelViewGroup>() {
    @Override public YourModelViewGroup inflateView() {
        return new YourModelViewGroup(getContext());
    }
};
        
 stackView.setAdapter(adapter);
 
```
  
  
[Reference](https://github.com/miguelbcr/OkAdapters/tree/master/app/src/main/java/app/stack_view) to a complete example.
