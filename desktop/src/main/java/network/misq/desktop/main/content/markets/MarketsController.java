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

package network.misq.desktop.main.content.markets;

import lombok.Getter;
import network.misq.application.DefaultServiceProvider;
import network.misq.desktop.common.threading.UIThread;
import network.misq.desktop.common.view.Controller;

public class MarketsController implements Controller {
    private final DefaultServiceProvider serviceProvider;
    private final MarketsModel model;
    @Getter
    private final MarketsView view;

    public MarketsController(DefaultServiceProvider serviceProvider) {
         this.serviceProvider = serviceProvider;
        model = new MarketsModel();
        view = new MarketsView(model, this);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void onViewAdded() {
    }

    @Override
    public void onViewRemoved() {
    }

    void onRefresh() {
        serviceProvider.getMarketPriceService().request()
                .whenComplete((marketPriceMap, t) -> UIThread.run(() -> model.setMarketPriceMap(marketPriceMap)));
    }
}