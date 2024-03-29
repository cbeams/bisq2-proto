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

package bisq.desktop.overlay.chat_rules;

import bisq.desktop.common.view.View;
import bisq.desktop.components.controls.UnorderedList;
import bisq.desktop.overlay.OverlayModel;
import bisq.i18n.Res;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatRulesView extends View<VBox, ChatRulesModel, ChatRulesController> {
    private final Button closeButton;
    private final UnorderedList content;

    public ChatRulesView(ChatRulesModel model, ChatRulesController controller) {
        super(new VBox(20), model, controller);

        root.setSpacing(20);
        root.setPrefWidth(OverlayModel.WIDTH);
        root.setPrefHeight(OverlayModel.HEIGHT);

        root.setPadding(new Insets(15, 30, 30, 30));

        Label headline = new Label(Res.get("chat.chatRules.headline"));
        headline.getStyleClass().add("bisq-text-headline-2");

        content = new UnorderedList(Res.get("chat.chatRules.content"), "bisq-text-13");

        closeButton = new Button(Res.get("action.close"));
        closeButton.setDefaultButton(true);

        VBox.setMargin(headline, new Insets(10, 0, 0, 0));
        VBox.setMargin(closeButton, new Insets(10, 0, 0, 0));
        root.getChildren().addAll(headline, content, closeButton);
    }

    @Override
    protected void onViewAttached() {
        closeButton.setOnAction(e -> controller.onClose());
    }

    @Override
    protected void onViewDetached() {
        closeButton.setOnAction(null);
    }
}
