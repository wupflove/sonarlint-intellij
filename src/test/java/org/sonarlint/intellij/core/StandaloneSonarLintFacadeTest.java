/*
 * SonarLint for IntelliJ IDEA
 * Copyright (C) 2015-2021 SonarSource
 * sonarlint@sonarsource.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonarlint.intellij.core;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.sonarlint.intellij.AbstractSonarLintLightTests;
import org.sonarsource.sonarlint.core.analysis.api.AnalysisResults;
import org.sonarsource.sonarlint.core.client.api.common.analysis.IssueListener;
import org.sonarsource.sonarlint.core.client.api.standalone.StandaloneAnalysisConfiguration;
import org.sonarsource.sonarlint.core.client.api.standalone.StandaloneRuleDetails;
import org.sonarsource.sonarlint.core.client.api.standalone.StandaloneSonarLintEngine;
import org.sonarsource.sonarlint.core.commons.log.ClientLogOutput;
import org.sonarsource.sonarlint.core.commons.progress.ClientProgressMonitor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StandaloneSonarLintFacadeTest extends AbstractSonarLintLightTests {
  private final StandaloneSonarLintEngine engine = mock(StandaloneSonarLintEngine.class);
  private StandaloneSonarLintFacade facade;

  @Before
  public void before() {
    facade = new StandaloneSonarLintFacade(getProject(), engine);
  }

  @Test
  public void should_get_rule_name() {
    var ruleDetails = mock(StandaloneRuleDetails.class);
    when(ruleDetails.getName()).thenReturn("name");
    when(engine.getRuleDetails("rule1")).thenReturn(Optional.of(ruleDetails));
    assertThat(facade.getRuleName("rule1")).isEqualTo("name");
    assertThat(facade.getRuleName("invalid")).isNull();
  }

  @Test
  public void should_get_rule_details() {
    var ruleDetails = mock(StandaloneRuleDetails.class);
    when(engine.getRuleDetails("rule1")).thenReturn(Optional.of(ruleDetails));
    assertThat(facade.getActiveRuleDetails("rule1")).isEqualTo(ruleDetails);
  }

  @Test
  public void should_get_description() {
    var ruleDetails = mock(StandaloneRuleDetails.class);
    when(ruleDetails.getHtmlDescription()).thenReturn("html");
    when(engine.getRuleDetails("rule1")).thenReturn(Optional.of(ruleDetails));
    assertThat(facade.getDescription("rule1")).isEqualTo("html");
    assertThat(facade.getDescription("invalid")).isNull();
  }

  @Test
  public void should_start_analysis() {
    var results = mock(AnalysisResults.class);
    when(engine.analyze(any(StandaloneAnalysisConfiguration.class), any(IssueListener.class), any(ClientLogOutput.class), any(ClientProgressMonitor.class))).thenReturn(results);
    assertThat(facade.startAnalysis(getModule(), Collections.emptyList(), mock(IssueListener.class), Collections.emptyMap(), mock(ClientProgressMonitor.class))).isEqualTo(results);
  }
}
