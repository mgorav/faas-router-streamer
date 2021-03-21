package com.gonnect.kafka.camel.router;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;


@Log4j2
public class K8sService {

    public static final String OPENFAAS_FN_NAMESPACE = "openfaas-fn";

    // TODO: Make this configurable auto refreshable cache
    public List<String> getAnnotatedFunctions() {
        final List<String> functionsBoundToATopic = new ArrayList<>();
        try (KubernetesClient client = new DefaultKubernetesClient()) {

            client.apps().deployments().inNamespace(OPENFAAS_FN_NAMESPACE).list().getItems().forEach(deployment -> {
                ObjectMeta metadata = deployment.getMetadata();
                if (metadata.getAnnotations().containsKey("topic") && metadata.getLabels().containsKey("faas_function")) {
                    functionsBoundToATopic.add(metadata.getLabels().get("faas_function"));
                }
            });
        }
        return functionsBoundToATopic;
    }

    public static void main(String[] args) {
        System.out.println(new K8sService().getAnnotatedFunctions());
    }
}

