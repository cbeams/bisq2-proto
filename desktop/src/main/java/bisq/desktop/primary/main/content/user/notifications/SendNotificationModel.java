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

package bisq.desktop.primary.main.content.user.notifications;

import bisq.desktop.common.view.Model;
import bisq.support.alert.AlertType;
import bisq.user.identity.UserIdentity;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;

@Slf4j
@Getter
public class SendNotificationModel implements Model {
    private final StringProperty selectedProfileUserName = new SimpleStringProperty();
    private final StringProperty privateKey = new SimpleStringProperty();
    private final StringProperty publicKey = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();
    private final BooleanProperty sendButtonDisabled = new SimpleBooleanProperty();
    private final ObjectProperty<AlertType> selectedAlertType = new SimpleObjectProperty<>();
    private final ObservableList<AlertType> alertTypes = FXCollections.observableArrayList();
    @Setter
    private UserIdentity userIdentity;
    @Setter
    private KeyPair keyPair;

    public SendNotificationModel() {
    }
}