/*
 * Copyright 2016 miguelbcr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package miguelbcr.ui.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Where the binding process takes place
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
class BindView<T, V extends View & OkRecyclerViewAdapter.Binder<T>> extends RecyclerView.ViewHolder {
    private final V view;

    public BindView(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}