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

package bisq.desktop.primary.main.content.trade.bisqEasy.chat.trade_assistant.offer;

import bisq.desktop.common.view.Model;
import bisq.offer.bisq_easy.BisqEasyOffer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TradeAssistantOfferModel implements Model {
    @Setter
    private BisqEasyOffer bisqEasyOffer;
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty direction = new SimpleStringProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty market = new SimpleStringProperty();
    private final StringProperty paymentMethods = new SimpleStringProperty();
    private final StringProperty baseSideAmount = new SimpleStringProperty();
    private final StringProperty quoteSideAmount = new SimpleStringProperty();
    private final StringProperty pricePremiumAsPercentage = new SimpleStringProperty();
    private final StringProperty pricePremiumInBaseCurrency = new SimpleStringProperty();
    private final StringProperty pricePremiumInQuoteCurrency = new SimpleStringProperty();
    private final StringProperty makersTradeTerms = new SimpleStringProperty();
    private final StringProperty requiredTotalReputationScore = new SimpleStringProperty();
    private final StringProperty minAmountAsPercentage = new SimpleStringProperty();
}
