package app.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.okadapters.R;
import library.recycler_view.OkRecyclerViewAdapter;

public class ItemViewGroup extends FrameLayout implements OkRecyclerViewAdapter.Binder<Item> {

    public ItemViewGroup(Context context) {
        super(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Bind(R.id.tv_value) TextView tv_value;
    @Override public void bind(Item item, int position, int total) {
        tv_value.setText(item.toString());
    }
}
