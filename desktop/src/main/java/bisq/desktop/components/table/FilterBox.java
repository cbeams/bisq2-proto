/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.desktop.components.table;

import bisq.i18n.Res;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;


@Slf4j
public class FilterBox {
    private final Controller controller;

    public FilterBox(FilteredList<? extends FilteredListItem> filteredList) {
        controller = new Controller(filteredList);
    }

    public HBox getRoot() {
        return controller.view.getRoot();
    }

    private static class Controller implements bisq.desktop.common.view.Controller {
        private final Model model;
        @Getter
        private final View view;

        private Controller(FilteredList<? extends FilteredListItem> filteredList) {
            model = new Model(filteredList);
            view = new View(model, this);
        }

        @Override
        public void onActivate() {
        }

        @Override
        public void onDeactivate() {
        }

        private void onSearch(String filterString) {
            model.filteredList.setPredicate(item -> model.defaultPredicate.test(item) && item.match(filterString));
        }
    }

    private static class Model implements bisq.desktop.common.view.Model {
        private final FilteredList<? extends FilteredListItem> filteredList;
        private final Predicate<FilteredListItem> defaultPredicate;

        private Model(FilteredList<? extends FilteredListItem> filteredList) {
            this.filteredList = filteredList;
            //noinspection unchecked
            defaultPredicate = (Predicate<FilteredListItem>) filteredList.getPredicate();
        }
    }


    @Slf4j
    public static class View extends bisq.desktop.common.view.View<HBox, Model, Controller> {
        private final ChangeListener<String> listener;
        private final TextField textField;

        private View(Model model, Controller controller) {
            super(new HBox(), model, controller);

            root.getStyleClass().add("bg-grey-5");
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(14, 24, 14, 24));
            root.setId("chat-filter-box");

            textField = new TextField();
            textField.setId("chat-input-field");
            textField.setPromptText(Res.get("action.search"));
            textField.setMinWidth(100);
            HBox.setHgrow(textField, Priority.ALWAYS);

            root.getChildren().add(textField);

            listener = (observable, oldValue, newValue) -> controller.onSearch(textField.getText());
        }

        @Override
        protected void onViewAttached() {
            textField.textProperty().addListener(listener);
            textField.setOnAction(e -> controller.onSearch(textField.getText()));
        }

        @Override
        protected void onViewDetached() {
            textField.textProperty().removeListener(listener);
            textField.setOnAction(null);
        }
    }
}