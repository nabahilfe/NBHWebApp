/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusErrorController {

    @GetMapping("/statuscode/403")
    public String accessDenied() {
        return "403"; // 403.html oder 403.jsp
    }
}
