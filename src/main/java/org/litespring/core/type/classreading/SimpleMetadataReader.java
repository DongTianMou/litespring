package org.litespring.core.type.classreading;
import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.litespring.core.type.MetadataReader;
import org.springframework.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleMetadataReader implements MetadataReader {

	private final Resource resource;

	private final ClassMetadata classMetadata;

	private final AnnotationMetadata annotationMetadata;

	public SimpleMetadataReader(Resource resource) throws IOException {
		InputStream is = new BufferedInputStream(resource.getInputStream());
		//asm提供
		ClassReader classReader;
		try {
			classReader = new ClassReader(is);
		}
		finally {
			is.close();
		}

		AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
		//read调用accept()方法时，visitor回逐步调用visitxxx()去读取字节码
		classReader.accept(visitor, ClassReader.SKIP_DEBUG);

		this.annotationMetadata = visitor;
		this.classMetadata = visitor;
		this.resource = resource;
	}


	public Resource getResource() {
		return this.resource;
	}

	public ClassMetadata getClassMetadata() {
		return this.classMetadata;
	}

	public AnnotationMetadata getAnnotationMetadata() {
		return this.annotationMetadata;
	}

}
