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

package bisq.desktop.overlay.onboarding;

import bisq.desktop.common.Transitions;
import bisq.desktop.common.utils.KeyHandlerUtil;
import bisq.desktop.common.view.NavigationView;
import bisq.desktop.overlay.OverlayModel;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnboardingView extends NavigationView<VBox, OnboardingModel, OnboardingController> {
    private Scene rootScene;

    public OnboardingView(OnboardingModel model, OnboardingController controller) {
        super(new VBox(), model, controller);

        root.setPrefWidth(OverlayModel.WIDTH);
        root.setPrefHeight(OverlayModel.HEIGHT);

        model.getView().addListener((observable, oldValue, newValue) -> {
            Region childRoot = newValue.getRoot();
            childRoot.setPrefHeight(root.getHeight());
            root.getChildren().add(childRoot);
            if (oldValue != null) {
                Transitions.transitLeftOut(childRoot, oldValue.getRoot());
            } else {
                Transitions.fadeIn(childRoot);
            }
        });
    }

    @Override
    protected void onViewAttached() {
        // Replace the key handler of OverlayView as we do not support escape/enter at onboarding
        rootScene = root.getScene();
        rootScene.setOnKeyReleased(keyEvent -> {
            KeyHandlerUtil.handleShutDownKeyEvent(keyEvent, controller::onQuit);
            KeyHandlerUtil.handleDevModeKeyEvent(keyEvent);
        });

        rootScene.getWindow().setWidth(OverlayModel.WIDTH);
        rootScene.getWindow().setHeight(OverlayModel.HEIGHT);
    }

    @Override
    protected void onViewDetached() {
        rootScene.setOnKeyReleased(null);
    }
}
