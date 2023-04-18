/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.config;

import co.rsk.remasc.RemascException;
import co.rsk.util.JacksonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Created by mario on 12/12/16.
 */
public class RemascConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger("RemascConfigFactory");

    private ObjectMapper mapper;
    private final JsonNode configNode;

    public RemascConfigFactory(String remascConfigFile) {
        this.mapper = new ObjectMapper();
        logger.info("RemascConfigFactory remascConfigFile = {} ", remascConfigFile);

        try (InputStream is = RemascConfigFactory.class.getClassLoader().getResourceAsStream(remascConfigFile)) {
            String sis = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            logger.info("RemascConfigFactory sis = {} ", sis);

            configNode = JacksonParserUtil.readTree(mapper, sis);

            logger.info("RemascConfigFactory node = {} ", configNode);
        } catch (Exception ex) {
            logger.error("Error reading REMASC", ex);
            throw new RemascException("Error reading REMASC [" + remascConfigFile + "]: ", ex);
        }
    }

    public RemascConfig createRemascConfig(String config) {
        RemascConfig remascConfig;

        logger.info("createRemascConfig config = {} ", config);

        try {
            logger.info("createRemascConfig node.get(config) = {} ", configNode.get(config));

            remascConfig = JacksonParserUtil.treeToValue(mapper, configNode.get(config), RemascConfig.class);

            logger.info("createRemascConfig remascConfig = {} ", remascConfig);
        } catch (Exception ex) {
            logger.error("Error reading REMASC configuration[{}]: {}", config, ex);
            throw new RemascException("Error reading REMASC configuration[" + config + "]: ", ex);
        }

        return remascConfig;
    }
}
