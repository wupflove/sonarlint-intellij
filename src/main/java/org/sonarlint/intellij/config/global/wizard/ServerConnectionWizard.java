/*
 * SonarLint for IntelliJ IDEA
 * Copyright (C) 2015-2020 SonarSource
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
package org.sonarlint.intellij.config.global.wizard;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.wizard.AbstractWizardEx;
import com.intellij.ide.wizard.AbstractWizardStepEx;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.sonarlint.intellij.config.global.ServerConnection;

public class ServerConnectionWizard {
  private final WizardModel model;
  private AbstractWizardEx wizard;

  public ServerConnectionWizard(ServerConnection connectionToEdit) {
    model = new WizardModel(connectionToEdit);
    init(model, true, Collections.emptySet());
  }

  public ServerConnectionWizard(Set<String> existingNames) {
    model = new WizardModel();
    init(model, false, existingNames);
  }

  private void init(WizardModel model, boolean editing, Set<String> existingNames) {
    List<AbstractWizardStepEx> steps = createSteps(model, editing, existingNames);
    String title = editing ? "Edit " : "New ";
    title = title + "Connection";
    wizard = new ServerConnectionWizardEx(steps, title);
  }

  private static List<AbstractWizardStepEx> createSteps(WizardModel model, boolean editing, Set<String> existingNames) {
    List<AbstractWizardStepEx> steps = new LinkedList<>();
    steps.add(new ServerStep(model, editing, existingNames));
    steps.add(new AuthStep(model));
    steps.add(new OrganizationStep(model));
    steps.add(new ConfirmStep(model, editing));
    return steps;
  }

  public boolean showAndGet() {
    return wizard.showAndGet();
  }

  public ServerConnection getServer() {
    return model.createServer();
  }

  private static class ServerConnectionWizardEx extends AbstractWizardEx {
    public ServerConnectionWizardEx(List<AbstractWizardStepEx> steps, String title) {
      super(title, null, steps);
      this.setHorizontalStretch(1.25f);
      this.setVerticalStretch(1.25f);
    }

    @Override
    protected void helpAction() {
      BrowserUtil.browse("https://github.com/SonarSource/sonarlint-intellij/wiki/Connected-Mode");
    }

    @Override
    protected String getDimensionServiceKey() {
      return this.getClass().getName();
    }
  }
}
