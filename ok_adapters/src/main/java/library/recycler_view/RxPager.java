/*
 * Copyright 2016 FuckBoilerplate
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

package library.recycler_view;

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxPager<T, V extends View & OkRecyclerViewAdapter.Binder<T>> implements OkRecyclerViewAdapter.LastItemListener {
    @LayoutRes private final int idResourceLoading;
    private final OkRecyclerViewAdapter<T, V> adapter;
    private final LoaderPager<T> loaderPager;
    private boolean allLoaded;
    private boolean stillLoading;

    public RxPager(@LayoutRes int idResourceLoading, LoaderPager<T> loaderPager, OkRecyclerViewAdapter<T, V> adapter) {
        this.idResourceLoading = idResourceLoading;
        this.loaderPager = loaderPager;
        this.adapter = adapter;
        showItems(loaderPager.onNextPage(null), false);
    }

    public int getIdResourceLoading() {
        return idResourceLoading;
    }

    @Override public void lastItemReached() {
        try {
            T item = adapter.getAll().get(adapter.getItemCount() - 2);
            nextPage(item);
        } catch (Exception i){}
    }


    void reset(Observable<List<T>> oItems) {
        showItems(oItems, true);
    }

    private void showItems(Observable<List<T>> oItems, final boolean reset) {
        stillLoading = true;
        oItems.subscribe(new Action1<List<T>>() {
            @Override public void call(final List<T> items) {
                new Handler().post(new Runnable() {
                    @Override public void run() {
                        stillLoading = false;
                        if (reset) adapter.setAll(items);
                        else adapter.addAll(items);
                    }
                });
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                stillLoading = false;
                throwable.printStackTrace();
            }
        });
    }

    private void nextPage(T item) {
        if (allLoaded) {
            adapter.removeMoreListener();
            return;
        }

        Observable<List<T>> oItems = loaderPager.onNextPage(item)
                .map(new Func1<List<T>, List<T>>() {
                    @Override public List<T> call(List<T> items) {
                        allLoaded = items.isEmpty();
                        return items;
                    }
                });

        showItems(oItems, false);
    }

    public boolean isStillLoading() {
        return stillLoading;
    }

    public interface LoaderPager<T> {
        Observable<List<T>> onNextPage(@Nullable T lastItem);
    }
}
