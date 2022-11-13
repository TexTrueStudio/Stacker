/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package io.github.Andrew6rant.stacker.api.fabricapi.event.lifecycle.v1;

import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.PlayerManager;
//import net.minecraft.server.network.ServerPlayerEntity;

import io.github.Andrew6rant.stacker.api.fabricapi.event.Event;
import io.github.Andrew6rant.stacker.api.fabricapi.event.EventFactory;

public final class ServerLifecycleEvents {
    private ServerLifecycleEvents() {
    }

    public static final Event<ServerStarted> SERVER_STARTED = EventFactory.createArrayBacked(ServerStarted.class, (callbacks) -> (server) -> {
        for (ServerStarted callback : callbacks) {
            callback.onServerStarted(server);
        }
    });

    public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = EventFactory.createArrayBacked(EndDataPackReload.class, callbacks -> (server, serverResourceManager, success) -> {
        for (EndDataPackReload callback : callbacks) {
            callback.endDataPackReload(server, serverResourceManager, success);
        }
    });

    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer server);
    }

    @FunctionalInterface
    public interface EndDataPackReload {
        void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success);
    }
}
