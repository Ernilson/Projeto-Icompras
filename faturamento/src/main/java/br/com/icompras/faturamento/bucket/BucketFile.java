package br.com.icompras.faturamento.bucket;

import java.io.InputStream;
import org.springframework.http.MediaType;

public record BucketFile(String name, InputStream is, MediaType type, long size) {
}
